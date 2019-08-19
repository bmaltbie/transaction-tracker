// Benjamin Maltbie - Final
// TransactionTracker Application Solution: buySceneController.java
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.event.ActionEvent;
import java.util.List;
import java.util.Arrays;
import java.lang.Long;
import java.lang.Character;
import java.lang.NumberFormatException;
import java.lang.ArrayIndexOutOfBoundsException;
import java.lang.StringIndexOutOfBoundsException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;


public class quickBuySceneController {
    @FXML private Label buySellLabel;
    @FXML private Label itemNameLabel;
    @FXML private Label priceLabel;
    @FXML private Label dateLabel;
    @FXML private Label quantityLabel;
    @FXML private ChoiceBox<String> accountChoiceBox;
    @FXML private CheckBox autoDateCheckBox;
    @FXML private TextArea userInputTextArea;
    @FXML private TextArea buyPreviewTextArea;
    @FXML private Button confirmButton;
    @FXML private Button clearButton;

    private List<String> formatBuy = Arrays.asList("b", "buy", "!buy", "nib", "inb");
    private List<String> formatSell = Arrays.asList("s", "sell", "!sell", "nis", "ins");
    private List<String> formatMinute = Arrays.asList("m", "min", "mins", "minute", "minutes");
    private List<String> formatHour = Arrays.asList("h", "hr", "hrs", "hour", "hours");
    private List<String> formatDay = Arrays.asList("d", "day", "days");
    private List<String> formatNow = Arrays.asList("now");
    private ObservableList<String> choiceList = FXCollections.observableArrayList();
    private ObservableList<Offer> buyList = FXCollections.observableArrayList();
    private ObservableList<Offer> sellList = FXCollections.observableArrayList();
    private DateTimeFormatter stdFormatter = DateTimeFormatter.ofPattern("M/d/yy h:mm a");
    
    
    @FXML
    public void initialize() {
        initializeAccountChoiceBox(); // initializes accounts choice box
        userInputTextArea.textProperty().addListener((observable,oldVal,newVal) -> {
            handleInput();
            handleClear();
        });
    } // initialize
    
    
    
    @FXML
    void clearButtonPressed(ActionEvent event) {
        boolean result = ConfirmBox.display("Clear", "Are you sure you want to clear?");
        
        if (result) {
            userInputTextArea.setText("");
            handleClear();
        } // if
    } // clearButtonPressed
    
    
    private void initializeAccountChoiceBox() {
        choiceList.removeAll(choiceList);
        
        for (String account : TransactionTracker.accountList)
            choiceList.add(account);
        
        accountChoiceBox.getItems().addAll(choiceList);
        accountChoiceBox.setValue(choiceList.get(0));
    } // initializeAccountChoiceBox
    
    
    private void handleClear() {
        if (userInputTextArea.getText() == null || userInputTextArea.getText().equals(""))
            clearButton.setDisable(true);
        else
            clearButton.setDisable(false);
    } // handleClear
    
    
    private void handleInput() {
        String [] parsedInfo = new String[5];
        boolean errorFound = false;
        String space = "   ";
        
        if (userInputTextArea.getText() == null || userInputTextArea.getText().equals(""))
            buyPreviewTextArea.setText("");
        else {
            buyPreviewTextArea.setText(""); // clear existing text
            buyList.clear(); // clear buyList
            sellList.clear(); // clear sellList
            
            String buyORsell = "?"; // to keep track of if the line is a buy or sell
            String itemName = "";
            long price = 0;
            int quantity = 0;
            LocalDateTime date = LocalDateTime.now();
            
            String [] lineTokens = userInputTextArea.getText().split("\n"); // separate by lines
            
            for (int i = 0; i < lineTokens.length; i++) {
                String [] tokens = lineTokens[i].split(" ");
                
                // check for buy or sell
                if (formatBuy.contains(tokens[0])) {
                    buyPreviewTextArea.appendText("BUY:" + space);
                    buyORsell = "b";
                }
                else if (formatSell.contains(tokens[0])) {
                    buyPreviewTextArea.appendText("SELL:" + space);
                    buyORsell = "s";
                }
                else
                    buyPreviewTextArea.appendText("[INVALID BUY/SELL]\t");
                
                
                // convert abbreviation to full item name
                if (tokens.length > 1 && TransactionTracker.abbreviations.containsKey(tokens[1].toLowerCase())) {
                    buyPreviewTextArea.appendText(TransactionTracker.abbreviations.get(tokens[1]) + space);
                    itemName = TransactionTracker.abbreviations.get(tokens[1]);
                }
                else
                    buyPreviewTextArea.appendText("[INVALID ITEM]\t");
                
                
                // decipher price
                try {
                    if (tokens.length > 2) {
                        price = readPrice(tokens[2]);
                        buyPreviewTextArea.appendText(commaDelineate_long(price) + space);
                    }
                    else
                        buyPreviewTextArea.appendText("[INVALID PRICE]\t");
                }
                catch (NumberFormatException e) {
                    buyPreviewTextArea.appendText("[INVALID PRICE]\t");
                } // catch
                
                
                // decipher quantity & date
                if (autoDateCheckBox.isSelected()) {
                    if (tokens.length == 3) {
                        // quantity assumed to be 1, date automatic
                        buyPreviewTextArea.appendText("x1" + space);
                        quantity = 1;
                    }
                    else if (tokens.length == 4) {
                        // read in quantity, automatic date
                        String quantityStr = tokens[3];
                        
                        if (quantityStr.substring(0,1).equals("x"))
                            quantityStr = quantityStr.substring(1, quantityStr.length());
                        
                        try {
                            int tempQuantity = Integer.parseInt(quantityStr);
                            quantity = tempQuantity;
                            buyPreviewTextArea.appendText("x" + String.valueOf(tempQuantity) + space);
                        }
                        catch (NumberFormatException e) {
                            buyPreviewTextArea.appendText("[INVALID QUANTITY]\t");
                        } // catch
                    }
                    else {
                        buyPreviewTextArea.appendText("[INVALID QUANTITY]\t");
                    } // else
                    
                    buyPreviewTextArea.appendText(LocalDateTime.now().format(stdFormatter));
                }
                else {
                    int dateIndex = 3;
                    boolean dateErrorFound = false;
                    
                    if (tokens.length < 4)
                        dateErrorFound = true;
                    else if (tokens.length == 4) {
                        // quantity assumed to be 1, read in date
                        buyPreviewTextArea.appendText("x1" + space);
                        quantity = 1;
                        dateIndex = 3;
                    }
                    else if (tokens.length == 5) {
                        dateIndex = 4;
                        String quantityStr = tokens[3];
                        
                        try {
                            if (quantityStr.substring(0,1).equals("x"))
                                quantityStr = quantityStr.substring(1, quantityStr.length());

                            int tempQuantity = Integer.parseInt(quantityStr);
                            quantity = tempQuantity;
                            buyPreviewTextArea.appendText("x" + String.valueOf(tempQuantity) + space);
                        }
                        catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                            buyPreviewTextArea.appendText("[INVALID QUANTITY]\t");
                        } // catch
                    }
                    else
                        dateErrorFound = true;
                    
                    
                    String temp = "";
                    boolean numFound = false;
                    int wordIndex = 0; // index where non-digit characters start
                    
                    
                    try {
                        for (int j = 0; j < tokens[dateIndex].length(); j++) {
                            if (Character.isDigit(tokens[dateIndex].charAt(j))) {
                                temp += tokens[dateIndex].charAt(j);
                                numFound = true;
                            }
                            else {
                                wordIndex = j;
                                break;
                            } // else
                        } // for
                        
                        if (numFound) {
                            long num = Long.parseLong(temp);
                            String remainingStr = tokens[dateIndex].substring(wordIndex, tokens[dateIndex].length());
                            
                            if (formatMinute.contains(remainingStr))
                                date = LocalDateTime.now().minusMinutes(num);
                            else if (formatHour.contains(remainingStr))
                                date = LocalDateTime.now().minusHours(num);
                            else if (formatDay.contains(remainingStr))
                                date = LocalDateTime.now().minusDays(num);
                            else if (formatNow.contains(remainingStr))
                                date = LocalDateTime.now();
                            else
                                dateErrorFound = true;
                        } // if
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                        dateErrorFound = true;
                    } // catch
                    
                    if (!numFound || dateErrorFound)
                        buyPreviewTextArea.appendText("[INVALID DATE]\t");
                    else
                        buyPreviewTextArea.appendText(date.format(stdFormatter));
                    
                } // else
                
//                String buyORsell = "?"; // to keep track of if the line is a buy or sell
//                String itemName = "";
//                long price = 0;
//                int quantity = 0;
//                LocalDateTime date = LocalDateTime.now();
                String account = accountChoiceBox.getValue();
                
                // account, quantity, itemName, buyDate, buyPrice, bought, sellPrice, sellDate
                Offer temp = new Offer(account, quantity, itemName, date, price, true, price, date);
                
                if (buyORsell.equals("b")) {
                    temp.setSellPrice_long(0L);
                    buyList.add(temp);
                }
                else if (buyORsell.equals("s"))
                    sellList.add(temp);
            
                buyPreviewTextArea.appendText("\n");
                
            } // for
        } // else
        
    } // handleInput
    
    
    private long readPrice(String price) {
        String tempStr = price.replace(",","");
        double tempDouble;
        long tempLong;
        
        // Allow for 'b', 'm' and 'k' shortcuts in price
        if (tempStr.substring(tempStr.length()-1, tempStr.length()).toLowerCase().equals("b")) {
            tempStr = tempStr.substring(0, tempStr.length()-1);
            tempDouble = Double.parseDouble(tempStr);
            tempLong = (long)(tempDouble*1000000000);
        }
        else if (tempStr.substring(tempStr.length()-1, tempStr.length()).toLowerCase().equals("m")) {
            tempStr = tempStr.substring(0, tempStr.length()-1);
            tempDouble = Double.parseDouble(tempStr);
            tempLong = (long)(tempDouble*1000000);
        }
        else if (tempStr.substring(tempStr.length()-1, tempStr.length()).toLowerCase().equals("k")) {
            tempStr = tempStr.substring(0, tempStr.length()-1);
            tempDouble = Double.parseDouble(tempStr);
            tempLong = (long)(tempDouble*1000);
        }
        else
            tempLong = Long.parseLong(tempStr);
        
        return tempLong;
    } // readPrice
    
    
    // This function takes in an long, turns it into a string and appropriately inserts commas
    private String commaDelineate_long(long str) {
        StringBuilder result = new StringBuilder(String.valueOf(str));
        int limit = (str < 0) ? 1 : 0; // if str < 0, set limit to 1, else set limit to 0
        
        for (int i = result.length()-3; i > limit; i -= 3)
            result.insert(i, ",");
        
        return (result.toString());
    } // commaDelineate_long

    
    @FXML
    void confirmButtonPressed(ActionEvent event) {
        if (buyPreviewTextArea.getText().contains("INVALID")) {
            MessageBox.display("Error", "There is an invalid buy/sell");
        }
        else {
            for (Offer off : buyList)
                TransactionTracker.activeList.add(new Offer(off));

            for (int i = 0; i < sellList.size(); i++) {
                for (int j = TransactionTracker.activeList.size()-1; j >= 0; j--) {
                    System.out.println(sellList.get(i).getQuantity());
                    if (sellList.get(i).equals(TransactionTracker.activeList.get(j))) {
                        System.out.println(sellList.get(i));
                        System.out.println(TransactionTracker.activeList.get(j));
                        if (TransactionTracker.activeList.get(j).getQuantity() > sellList.get(i).getQuantity()) {
                            Offer temp = new Offer(TransactionTracker.activeList.get(j));
                            TransactionTracker.activeList.get(j).setQuantity(TransactionTracker.activeList.get(j).getQuantity()
                                                                             -sellList.get(i).getQuantity());
                            
                            temp.setQuantity(sellList.get(i).getQuantity());
                            temp.setBought(true);
                            temp.setSellPrice_long(sellList.get(i).getSellPrice_long());
                            temp.setSellDate(sellList.get(i).getSellDate_date());
                            TransactionTracker.completeList.add(temp);
                            break;
                        } // if sold fewer than bought, will split it up
                        else if (TransactionTracker.activeList.get(j).getQuantity() < sellList.get(i).getQuantity()) {
                            TransactionTracker.activeList.get(j).setBought(true);
                            TransactionTracker.activeList.get(j).setSellPrice_long(sellList.get(i).getSellPrice_long());
                            TransactionTracker.activeList.get(j).setSellDate(sellList.get(i).getSellDate_date());
                            TransactionTracker.completeList.add(new Offer(TransactionTracker.activeList.remove(j)));
                            sellList.get(i).setQuantity(sellList.get(i).getQuantity()
                                                        -TransactionTracker.activeList.get(j).getQuantity());
                        } // else if sold more than this one buy
                        else {
                            // sold same number as bought
                            TransactionTracker.activeList.get(j).setBought(true);
                            TransactionTracker.activeList.get(j).setSellPrice_long(sellList.get(i).getSellPrice_long());
                            TransactionTracker.activeList.get(j).setSellDate(sellList.get(i).getSellDate_date());
                            TransactionTracker.completeList.add(new Offer(TransactionTracker.activeList.remove(j)));
                            break;
                        } // else
                    } // if equal
                } // for j
            } // for i
            
            
            userInputTextArea.setText("");
            MessageBox.display("Success", "Buys and sells added!");
            
            
            FXCollections.sort(TransactionTracker.activeList, new OfferDateComparator());
            FXCollections.sort(TransactionTracker.completeList, new OfferDateComparator());
            
            buyList.clear();
            sellList.clear();
        } // else
        
    } // confirmButtonPressed
    
    
    
    
} // quickBuySceneController
















