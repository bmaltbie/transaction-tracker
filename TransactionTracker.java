// Benjamin Maltbie - Final
// TransactionTracker Application Solution: TransactionTracker.java
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;


// Main application
public class TransactionTracker extends Application {
    private Stage primaryStage;
    public static String chosenItem = "default";
    private static BorderPane mainLayout;
    
    // These booleans and lists are used to keep track of the information in the various tables
    // There are in this class and static so the information can be accessed by every scene
    //public static boolean offersInitialized = false;
    public static boolean recentInitialized = false;
    public static ObservableList<Offer> activeList = FXCollections.observableArrayList();
    public static ObservableList<Offer> recentList = FXCollections.observableArrayList();
    public static ObservableList<Offer> completeList = FXCollections.observableArrayList();
    public static ObservableList<String> accountList = FXCollections.observableArrayList();

    public static Map<String, ArrayList<String>> categories = new HashMap<String, ArrayList<String>>();
    public static Map<String, String> itemDB = new HashMap<String, String>(); // name -> itemID
    public static Map<String, String> abbreviations = new HashMap<String, String>(); // abbr -> name
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TransactionTracker");
        
        initializeActiveList();
        initializeComplete();
        initializeAccountList();
        initializeCategories();
        initializeItemDB_Abbreviations();
        
        // Always see MainScene and one other scene
        showMainScene();
        showHomeScene();
        
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
    } // start
    
    
    // This scene is the toolbar at the top with the two combo boxes
    private void showMainScene() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionTracker.class.getResource("TransactionTracker.fxml"));
        mainLayout = loader.load();
        Scene scene = new Scene(mainLayout);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    } // showMainScene
    
    
    // This is the default second scene, shows active offers and watch list
    public static void showHomeScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionTracker.class.getResource("homeScene.fxml"));
        BorderPane homeSceneBP = loader.load();
        mainLayout.setCenter(homeSceneBP);
    } // showHomeScene
    
    
    // This scene gives the user an option to add a new buy offer, which gets added to activeOffers
    public static void showBuyScene(String str) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionTracker.class.getResource("buyScene.fxml"));
        BorderPane buySceneBP = loader.load();
        mainLayout.setCenter(buySceneBP);
        
//        buySceneController controller = loader.getController();
//        controller.initializeData(str);
    } // showBuyScene
    
    
    // This scene shows users all completed offers and the total profit they have earned
    public static void showHistoryScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionTracker.class.getResource("historyScene.fxml"));
        BorderPane historySceneBP = loader.load();
        mainLayout.setCenter(historySceneBP);
    } // showHistoryScene
    
    
    public static void showQuickBuyScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionTracker.class.getResource("quickBuyScene.fxml"));
        BorderPane quickBuySceneBP = loader.load();
        mainLayout.setCenter(quickBuySceneBP);
    }  // showQuickBuyScene
    
    
    
    // Initializes activeList, the observableArrayList in TransactionTracker, which holds all
    // current offers and populates activeOffersTable in homeScene.
    private void initializeActiveList() {
        String line, _account, _itemName, _buyPrice, _sellPrice, temp;
        String [] offerTokens;
        int _quantity;
        LocalDateTime _buyDate;
        boolean _bought;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Offer.getFormatter());
        
        // Reads activeOffer.txt and breaks down each line into the proper components. Uses
        // these components to create a new Offer object an insert it into activeList
        try(Scanner input = new Scanner(Paths.get("activeOffer.txt"))) {
            while (input.hasNextLine()) {
                line = input.nextLine();
                offerTokens = line.split(" ");
                
                _account = offerTokens[0];
                _quantity = Integer.parseInt(offerTokens[1]);
                _itemName = offerTokens[2].replace("_", " ");
                
                temp = offerTokens[3] + " " + offerTokens[4] + " " + offerTokens[5];
                _buyDate = LocalDateTime.parse(temp, formatter);
                
                _buyPrice = offerTokens[6];
                _bought = Boolean.valueOf(offerTokens[7]);
                _sellPrice = offerTokens[8];
                
                activeList.add(new Offer(_account, _quantity, _itemName, _buyDate, _buyPrice,
                                         _bought, _sellPrice));
            } // while
            
            FXCollections.sort(activeList, new OfferDateComparator());
        }
        catch (IOException | NoSuchElementException | IllegalStateException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        } // catch
        
    } // initializeActiveList
    
    
    
    
    // This function reads in CompleteOffer objects from completeOffer.txt and initializes
    // completeList in TransactionTracker.
    private void initializeComplete() {
        String line, _account, _itemName, _buyPrice, _sellPrice, temp;
        String [] offerTokens;
        int _quantity;
        LocalDateTime _buyDate, _sellDate;
        boolean _bought;
        long _profit;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Offer.getFormatter());
        

        try(Scanner input = new Scanner(Paths.get("completeOffer.txt"))) {
            while (input.hasNextLine()) {
                line = input.nextLine();
                offerTokens = line.split(" ");
                
                _account = offerTokens[0];
                _quantity = Integer.parseInt(offerTokens[1]);
                _itemName = offerTokens[2].replace("_", " ");
                
                
                temp = offerTokens[3] + " " + offerTokens[4] + " " + offerTokens[5];
                _buyDate = LocalDateTime.parse(temp, formatter);
                
                _buyPrice = offerTokens[6];
                _bought = Boolean.valueOf(offerTokens[7]);
                _sellPrice = offerTokens[8];
                
                temp = offerTokens[9] + " " + offerTokens[10] + " " + offerTokens[11];
                _sellDate = LocalDateTime.parse(temp, formatter);
                
                _profit = Long.parseLong(offerTokens[12]);
                
                completeList.add(0, new Offer(_account, _quantity, _itemName, _buyDate, _buyPrice,
                                              _bought, _sellPrice, _sellDate, _profit));
            } // while
            
            FXCollections.sort(completeList, new OfferDateComparator());
        }
        catch (IOException | NoSuchElementException | IllegalStateException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        } // catch
        
    } // initializeComplete
    
    
    private void initializeAccountList() {
        try(Scanner input = new Scanner(Paths.get("Accounts.txt"))) {
            while (input.hasNext())
                accountList.add(input.next());
        }
        catch (IOException | NoSuchElementException | IllegalStateException e) {
            e.printStackTrace();
        } // catch
    } // initializeAccountList
    
    
    private void initializeCategories() {
        String line;
        String [] tokens;
        
        try(Scanner input = new Scanner(Paths.get("Categories.txt"))) {
            while (input.hasNextLine()) {
                ArrayList<String> temp = new ArrayList<String>();
                line = input.nextLine();
                tokens = line.split(" ");
                
                // Names are stored with underscores instead of spaces, replaces them with spaces
                for (int j = 0; j < tokens.length; j++)
                    tokens[j] = tokens[j].replace("_", " ");
                
                for (int i = 1; i < tokens.length; i++)
                    temp.add(tokens[i]);
                
                categories.put(tokens[0], temp); // add categories + items into hash map
            } // while
        } // try
        catch (IOException | NoSuchElementException | IllegalStateException e) {
            e.printStackTrace();
        } // catch
    } // initializeCategories
    
    
    
    private void initializeItemDB_Abbreviations() {
        String line;
        String [] tokens;
        
        try(Scanner input = new Scanner(Paths.get("itemDB.txt"))) {
            while (input.hasNextLine()) {
                line = input.nextLine();
                tokens = line.split(" ");
                
                // Names are stored with underscores instead of spaces, replaces them with spaces
                tokens[0] = tokens[0].replace("_", " ");
                
                // Put in hash map
                itemDB.put(tokens[0], tokens[1]); // name -> itemID
                abbreviations.put(tokens[2], tokens[0]); // abbreviations -> name
            } // while
        }
        catch (IOException | NoSuchElementException | IllegalStateException e) {
            e.printStackTrace();
        } // catch
    } // initializeItemDB_Abbreviations
    
    
    
    // This function activates when the user tries to close the program either through "Exit..."
    // in the Menu or through the 'X' button. Shows a pop-up window to confirm that the user
    // does want to exit. If yes, saves all new data and updates the source files, otherwise
    // does nothing.
    public void closeProgram() {
        boolean result = ConfirmBox.display("Exit", "Are you sure you want to exit?");
        
        if (result) {
            primaryStage.close();
            saveCompleteOffers();
            saveActiveOffers();
            saveAccounts();
            saveCategories();
            saveItemDB_Abbreviations();
        } // if
    } // closeProgram
    
    
    public static void main(String[] args) {
        launch(args);
    } // main
    
    
    // This function saves all completed offers and writes them to completeOffer.txt to store
    // the information. It also gets all the active offers that are now completed, removes them
    // from active offers and adds them to completed offers.
    private void saveCompleteOffers() {
        String _account, _itemName, _buyPrice, _sellPrice;
        int _quantity;
        LocalDateTime _buyDate, _sellDate;
        boolean _bought, bought;
        long _profit;
        
        try (Formatter output = new Formatter("completeOffer.txt")) {
            for (int i = activeList.size()-1; i >= 0; i--) {
                bought = activeList.get(i).getBought();
                
                if (bought) {
                    _account = activeList.get(i).getAccount();
                    _quantity = activeList.get(i).getQuantity();
                    _itemName = activeList.get(i).getItemName();
                    _buyDate = activeList.get(i).getBuyDate_date();
                    _buyPrice = activeList.get(i).getBuyPrice_str();
                    _bought = activeList.get(i).getBought();
                    _sellPrice = activeList.get(i).getSellPrice_str();
                    _sellDate = activeList.get(i).getSellDate_date();
                    _profit = (activeList.get(i).getSellPrice_long() - activeList.get(i).getBuyPrice_long()) * _quantity;
                    
                    completeList.add(new Offer(_account, _quantity, _itemName, _buyDate, _buyPrice,
                                               _bought, _sellPrice, _sellDate, _profit));
                    activeList.remove(i);
                } // if
            } // for
                
            for (int i = 0; i < completeList.size(); i++) {
                output.format("%s%n", completeList.get(i));
            } // for
            
        }
        catch (SecurityException | FileNotFoundException | FormatterClosedException e) {
            e.printStackTrace();
            System.exit(1); // terminate the program
        } // catch
        
    } // saveCompleteOffers
    
    
    
    // This function saves all active offers and writes them to activeOffer.txt to store
    // the information.
    private void saveActiveOffers() {
        try (Formatter output = new Formatter("activeOffer.txt")) {
            for (int i = 0; i < activeList.size(); i++)
                output.format("%s%n", activeList.get(i).toString_active());
        }
        catch (SecurityException | FileNotFoundException | FormatterClosedException e) {
            e.printStackTrace();
            System.exit(1); // terminate the program
        } // catch
        
    } // saveActiveOffers
    
    
    private void saveAccounts() {
        try (Formatter output = new Formatter("Accounts.txt")) {
            for (int i = 0; i < accountList.size(); i++)
                output.format("%s ", accountList.get(i));
        }
        catch (SecurityException | FileNotFoundException | FormatterClosedException e) {
            e.printStackTrace();
            System.exit(1); // terminate the program
        } // catch
        
    } // saveAccounts
    
    
    private void saveCategories() {
        try (Formatter output = new Formatter("Categories.txt")) {
            Object [] cats = categories.keySet().toArray();
            
            for (int i = 0; i < categories.size(); i++) {
                output.format("%s", cats[i].toString().replaceAll(" ", "_"));
                
                for (int j = 0; j < categories.get(cats[i]).size(); j++)
                    output.format(" %s", categories.get(cats[i]).get(j).replaceAll(" ", "_"));
                
                output.format("%n");
            } // for
        }
        catch (SecurityException | FileNotFoundException | FormatterClosedException e) {
            e.printStackTrace();
            System.exit(1); // terminate the program
        } // catch
    } // saveCategories
    
    // abbrev -> name
    // name -> itemID
    private void saveItemDB_Abbreviations() {
        try (Formatter output = new Formatter("itemDB.txt")) {
            Object [] keys = abbreviations.keySet().toArray();
            for (int i = 0; i < abbreviations.size(); i++)
                output.format("%s %s %s%n", abbreviations.get(keys[i].toString()).replace(" ", "_"),
                              itemDB.get(abbreviations.get(keys[i].toString())), keys[i]);
        }
        catch (SecurityException | FileNotFoundException | FormatterClosedException e) {
            e.printStackTrace();
            System.exit(1); // terminate the program
        } // catch
    } // saveItemDB_Abbreviations
    
    
    
} // TransactionTracker


