// Benjamin Maltbie - Final
// TransactionTracker Application Solution: buySceneController.java
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.IOException;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.IllegalStateException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.NoSuchElementException;
import java.lang.StringBuilder;
import javafx.scene.paint.Color;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.lang.Math;


// This is the controller for the add buy scene, which appears when the add buy button is pressed.
// It allows the user to create a new Offer for the item that is currently selected in the
// item choice box. If the user wants to select a different item, they can change the item selected
// in the item choice box and then press the refresh button. The information that appears based on
// the selected item comes from two sources: itemDB.txt and scraped off the Runescape Wikipedia page.
// itemDB contains all static information about the item that never changes. The information scraped
// off the Wikipedia page dynamically changes hourly and so must be updated whenever the user tries
// to add a new buy in order for them to view the latest information. The graph is composed from
// data that is gathered when scraped off the Wikipedia page.
public class buySceneController {
    @FXML private Label itemNameLabel;
    @FXML private Button refreshButton;
    @FXML private Button addButton;
    @FXML private Button rswikiButton;
    @FXML private Button geButton;
    @FXML private TableView<Offer> recentHistoryTable;
    @FXML private TableColumn<Offer, String> dateColumn;
    @FXML private TableColumn<Offer, String> buyPriceColumn;
    @FXML private TableColumn<Offer, String> sellPriceColumn;
    @FXML private ChoiceBox<String> accountChoiceBox;
    @FXML private TextField buyPriceTextField;
    @FXML private Label buyLimitLabel;
    @FXML private TextField quantityTextField;
    @FXML private Label gePriceLabel;
    @FXML private Label geHighLabel;
    @FXML private Label geLowLabel;
    @FXML private Label _30DayPriceLabel;
    @FXML private Label _90DayPriceLabel;
    @FXML private Label _180DayPriceLabel;
    @FXML private LineChart<String, Long> geLineChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    
    private ObservableList<String> choiceList = FXCollections.observableArrayList();
    private static OkHttpClient client = new OkHttpClient(); // class from external .JAR file
    private TransactionTracker transactionTracker;
    private boolean infoInitialized = false;
    public static boolean onBuyScene = false;
    
    
    @FXML
    public void initialize() {
        try {
            onBuyScene = true;
            initializeChoiceBox(); // initializes accounts choice box
            
            // initialize data based on item in item choicebox
            initializeData(TransactionTracker.chosenItem);
            
            // Only needs to initialize recent offer list once, from that point on we can populate
            // the table from the existing list without having to re-open completeOffer.txt
            if (!TransactionTracker.recentInitialized)
                initializeRecent(TransactionTracker.chosenItem);
            
            initializeRecentHistoryTable();
        }
        catch (Exception e) {
            e.printStackTrace();
        } // catch
    } // initialize
    
    
    // Gets all static information about the selected item from itemDB.txt and sets the
    // corresponding labels to reflect that information
    public void initializeData(String itemName) {
        itemNameLabel.setText(itemName);
        String itemId;
        int index = 0;
        String [] specificItem;
        
        if (!infoInitialized) {
            try {
                infoInitialized = true;
                itemId = TransactionTracker.itemDB.get(itemName);
                
                specificItem = getItemData(itemId);
                
                String [] rawTokens = specificItem[1].split("\n");
                
                index = indexOf(rawTokens, "price");
                long price = getPrice(rawTokens[index]);
                String priceStr = commaDelineate(price);
                gePriceLabel.setText(priceStr);
                
                index = indexOf(rawTokens, "limit");
                int limit = getBuyLimit(rawTokens[index]);
                buyLimitLabel.setText(commaDelineate_limit(limit));
                
                // Sets labels
                _30DayPriceLabel.setText(specificItem[2]);
                _90DayPriceLabel.setText(specificItem[3]);
                _180DayPriceLabel.setText(specificItem[4]);
                
                // Changes the text to appropriate color [+:green, -:red, 0:black]
                chooseColor(_30DayPriceLabel, specificItem[2]);
                chooseColor(_90DayPriceLabel, specificItem[3]);
                chooseColor(_180DayPriceLabel, specificItem[4]);

                
                // Initialize graph // specificItem[5]
                String dailyDate, averageDate;
                long dailyPrice, averagePrice;
                geLineChart.getData().clear();
                
                XYChart.Series dailySeries = new XYChart.Series();
                dailySeries.setName("Daily");
                //dailySeries.setVerticalGridLinesVisible(false);
                
                XYChart.Series averageSeries = new XYChart.Series();
                averageSeries.setName("Average");
                
                String [] graphTokens = specificItem[5].split("average");
                graphTokens[0] = graphTokens[0].substring(10, graphTokens[0].length()-3);
                graphTokens[1] = graphTokens[1].substring(3, graphTokens[1].length()-2);
                
                String [] dailyTokens = graphTokens[0].split(",");
                String [] averageTokens = graphTokens[1].split(",");
                
                DateFormat df = new SimpleDateFormat("M/d/yy");
                
                long lowerBoundPrice = Integer.MAX_VALUE;
                long upperBoundPrice = 0;
                
                for (int i = 0; i < dailyTokens.length; i++) {
                    dailyDate = dailyTokens[i].substring(dailyTokens[i].indexOf('"')+1,
                                                         dailyTokens[i].lastIndexOf('"'));
                    dailyPrice = Long.parseLong(dailyTokens[i].substring(dailyTokens[i].indexOf(':')+1,
                                                                           dailyTokens[i].length()));
                    
                    
                    if (dailyPrice < lowerBoundPrice)
                        lowerBoundPrice = dailyPrice;
                    else if (dailyPrice > upperBoundPrice)
                        upperBoundPrice = dailyPrice;
                    
                    Date date = new Date(Long.parseLong(dailyDate));
                    String dateStr = df.format(date);
                    
                    dailySeries.getData().add(new XYChart.Data(dateStr, dailyPrice));
                } // for
                
                String lowPrice = commaDelineate(Math.toIntExact(lowerBoundPrice));
                String highPrice = commaDelineate(Math.toIntExact(upperBoundPrice));
                
                geLowLabel.setText(lowPrice);
                geHighLabel.setText(highPrice);
                
                int lowerDivisor = (upperBoundPrice < 5000) ? 10 : 40;
                int upperDivisor = (upperBoundPrice < 5000) ? 20 : 60;
                
                lowerBoundPrice = lowerBoundPrice - (lowerBoundPrice/lowerDivisor);
                upperBoundPrice = upperBoundPrice + (upperBoundPrice/upperDivisor);
                
                if (lowerBoundPrice < 0)
                    lowerBoundPrice = 0;
                
                if (upperBoundPrice < 10)
                    upperBoundPrice++;
                
                yAxis.setAutoRanging(false);
                yAxis.setLowerBound(lowerBoundPrice);
                yAxis.setUpperBound(upperBoundPrice);
                yAxis.setTickUnit((upperBoundPrice-lowerBoundPrice)/8);
                
                for (int i = 0; i < averageTokens.length; i++) {
                    averageDate = averageTokens[i].substring(averageTokens[i].indexOf('"')+1,
                                                         averageTokens[i].lastIndexOf('"'));
                    averagePrice = Integer.parseInt(averageTokens[i].substring(averageTokens[i].indexOf(':')+1,
                                                                           averageTokens[i].length()));
                    
                    Date date = new Date(Long.parseLong(averageDate));
                    String dateStr = df.format(date);
                    
                    averageSeries.getData().add(new XYChart.Data(dateStr, averagePrice));
                } // for
                
                geLineChart.getData().addAll(averageSeries, dailySeries);
                
            }
            catch (Exception e) {
                e.printStackTrace();
            } // catch
        } // if

    } // initializeData
    
    
    // Chooses color based on if the value is "positive" or "negative" or "neutral"
    private void chooseColor(Label label, String str) {
        if (str.substring(0,4).equals("+0.0") || str.substring(0,3).equals("0.0"))
            label.setTextFill(Color.BLACK);
        else if (str.substring(0,1).equals("+"))
            label.setTextFill(Color.GREEN);
        else
            label.setTextFill(Color.RED);
    } // chooseColor
    
    
    // Initializes accountChoiceBox by reading Accounts.txt. This is a separate file because
    // in the future I will add the functionality for the user to add more accounts.
    private void initializeChoiceBox() {
        choiceList.removeAll(choiceList);
        
        for (String account : TransactionTracker.accountList)
            choiceList.add(account);
        
        accountChoiceBox.getItems().addAll(choiceList);
        accountChoiceBox.setValue(choiceList.get(0));
    } // initializeChoiceBox
    
    
    
    // This function is used to make the buy/sell price easier to read by inserting commas
    // as well as including the units of the currency
    private String commaDelineate(int str) {
        StringBuilder result = new StringBuilder(String.valueOf(str));
        
        for (int i = result.length()-3; i > 0; i -= 3)
            result.insert(i, ",");

        result.append(" gp");
        
        return (result.toString());
    } // commaDelineate
    
    
    private String commaDelineate_limit(int str) {
        StringBuilder result = new StringBuilder(String.valueOf(str));
        
        for (int i = result.length()-3; i > 0; i -= 3)
            result.insert(i, ",");

        return (result.toString());
    } // commaDelineate_limit
    
    
    private String commaDelineate(long str) {
        StringBuilder result = new StringBuilder(String.valueOf(str));
        
        for (int i = result.length()-3; i > 0; i -= 3)
            result.insert(i, ",");
        
        result.append(" gp");
        
        return (result.toString());
    } // commaDelineate
    
    
    // Helper function for parsing the JSON information that is returned from accessing
    // Runescape Wikipedia's API
    public static long getPrice(String s) {
        int start = s.indexOf("= ");
        String result = s.substring(start+2, s.length()-1);
        long price = Long.parseLong(result);
        
        return price;
    } // getPrice
    
    
    // Helper function for parsing the JSON information that is returned from accessing
    // Runescape Wikipedia's API
    public static int getBuyLimit(String s) {
        int buyLimit;
        int start = s.indexOf("= ");
        String result = s.substring(start+2, s.length()-1);
        
        if (result.equals("nil"))
            buyLimit = 999;
        else
            buyLimit = Integer.parseInt(result);
        
        return buyLimit;
    } // getBuyLimit
    
    
    // Function for making an HTTP request. Returns the result (in this case, an JSON result)
    public static String getJSON(String url) throws IOException {
        Request request = new Request.Builder()
                                     .url(url)
                                     .build();
        
        Response response = client.newCall(request).execute();
        return response.body().string();
    } // getJSON
    
    
    // Helper function for seeing if a certain string line contains a word
    // Can replace with regionMatches?
    public static boolean contains(String s, String word) {
        s = s.trim();
        
        if (word.length() > s.length())
            return false;
        else
            return (s.substring(0, word.length()).equals(word));
    } // contains
    
    
    // Helper function for determining which index of an array contains a particular word
    public static int indexOf(String [] str, String word) {
        int index = 0;
        
        for (int i = 0; i < str.length; i++) {
            if (contains(str[i], word)) {
                index = i;
                break;
            } // if
        } // for
        
        return index;
    } // indexOf
    
    
    // This function is the main function for getting information from the Runescape Wikipedia.
    // It gets the information using the getJSON function and then returns specific parts of it
    // in an organized string array.
    public static String [] getItemData(String itemID) {
        String json = null;
        String raw = null;
        String name = null;
        String graphData = null;
        
        try {
            json = getJSON("http://services.runescape.com/m=itemdb_rs/api/catalogue/detail.json?item="
                           + itemID);
            Gson gson = new Gson();
            RSWikiAPI rswikiapi = gson.fromJson(json, RSWikiAPI.class);
            name = rswikiapi.getItem().getName();
            name = name.replaceAll(" ", "_");
            
            raw = "http://runescape.wikia.com/wiki/Module:Exchange/"+name+"?action=raw";
            raw = getJSON(raw);
            
            graphData = "http://services.runescape.com/m=itemdb_rs/api/graph/" + itemID + ".json";
            graphData = getJSON(graphData);
            
            return new String[] {
                rswikiapi.getItem().getName(),
                raw,
                rswikiapi.getItem().getDay30().getChange(),
                rswikiapi.getItem().getDay90().getChange(),
                rswikiapi.getItem().getDay180().getChange(),
                graphData
            };
        }
        catch (Exception e) {
            return new String[] {"Failed"};
            // e.printStackTrace();
        } // catch

    } // getItemData
    
    
    // When the add button is pressed, the information entered is taken and used to create an
    // ActiveOffer object which is then added to the ActiveOffer table in the home scene. It then
    // switches back to the home scene as the user is presumably done trying to add that item.
    @FXML
    void addButtonPressed(ActionEvent event) {        
        String _account = accountChoiceBox.getValue();
        int _quantity = Integer.parseInt(quantityTextField.getText());
        String _itemName = itemNameLabel.getText();
        LocalDateTime _buyDate = LocalDateTime.now();
        String _buyPrice = buyPriceTextField.getText();
        boolean _bought = false;
        String _sellPrice = "0";
        
        // Add new item to ActiveOffer activeList
        TransactionTracker.activeList.add(0, new Offer(_account, _quantity, _itemName, _buyDate,
                                                       _buyPrice, _bought, _sellPrice));
        try {
            transactionTracker.showHomeScene(); // return to home scene
        }
        catch (Exception e) {
            e.printStackTrace();
        } // catch
    } // addButtonPressed
    
    
    // When the refresh button is pressed, the newly selected item in the item choice box's
    // information is populated onto the screen.
    @FXML
    void refreshButtonPressed(ActionEvent event) {
        infoInitialized = false;
        initializeData(TransactionTracker.chosenItem);
        initializeRecent(TransactionTracker.chosenItem);
        // refresh();
    } // refreshButtonPressed
    
    
//    public void refresh() {
//        infoInitialized = false;
//        initializeData(TransactionTracker.chosenItem);
//        initializeRecent(TransactionTracker.chosenItem);
//    } // refresh
    
    
    // Opens a WebEngine window that contains the RuneScape Grand Exchange page based on item
    @FXML
    void geButtonPressed(ActionEvent event) {
        String ID = TransactionTracker.itemDB.get(TransactionTracker.chosenItem);
        String name = TransactionTracker.chosenItem.replace(" ", "_");
        String url = "http://services.runescape.com/m=itemdb_rs/a=62/"+name+"/viewitem?obj="+ID;
        WebBrowser.display(url);
    } // geButtonPressed
    
    
    // Opens a WebEngine window that contains the RuneScape Wikipedia page based on item selected
    @FXML
    void rswikiButtonPressed(ActionEvent event) {
        String name = TransactionTracker.chosenItem.replace(" ", "_");
        
        if (name.substring(name.length()-1, name.length()).equals(")")) // get rid of potion doses
            name = name.substring(0, name.length()-4);
        
        String url = "http://runescape.wikia.com/wiki/"+name;
        WebBrowser.display(url);
    } // rswikiButtonPressed
    
    
    // Initialize recent initializes the recentHistory table which shows the latest completed
    // transactions so the user can see what they previously paid for for the items. It only shows
    // the recent history of the selected item, not all recent history. It finds this information
    // by reading completeOffer.txt
    private void initializeRecent(String name_) {
        String _account, _itemName, _buyPrice, _sellPrice;
        int _quantity;
        LocalDateTime _buyDate, _sellDate;
        boolean _bought;
        long _profit;

        TransactionTracker.recentList.clear(); // clears list because only want to see specific item
        
        for (int i = 0; i < TransactionTracker.completeList.size(); i++) {
            if (TransactionTracker.completeList.get(i).getItemName().equals(name_)) {
                _account = TransactionTracker.completeList.get(i).getAccount();
                _quantity = TransactionTracker.completeList.get(i).getQuantity();
                _itemName = TransactionTracker.completeList.get(i).getItemName();
                _buyDate = TransactionTracker.completeList.get(i).getBuyDate_date();
                _buyPrice = TransactionTracker.completeList.get(i).getBuyPrice_str();
                _bought = TransactionTracker.completeList.get(i).getBought();
                _sellPrice = TransactionTracker.completeList.get(i).getSellPrice_str();
                _sellDate = TransactionTracker.completeList.get(i).getSellDate_date();
                _profit = TransactionTracker.completeList.get(i).getProfit_long();
                
                TransactionTracker.recentList.add(new Offer(_account, _quantity, _itemName,
                                                            _buyDate, _buyPrice, _bought,
                                                            _sellPrice, _sellDate, _profit));
            } // if
        } // for
        
        TransactionTracker.recentInitialized = true;
    } // initializeRecent
    
    
    // Initializes recentHistoryTable
    private void initializeRecentHistoryTable() {
        if (TransactionTracker.recentInitialized) {
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateRange"));
            buyPriceColumn.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
            sellPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
            
            recentHistoryTable.setItems(TransactionTracker.recentList);
        } // if
    } // initializeRecentHistoryTable
    
    
} // buySceneController

