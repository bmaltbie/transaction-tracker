// Benjamin Maltbie - Final
// TransactionTracker Application Solution: TransactionTrackerController.java
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.lang.IllegalStateException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.NoSuchElementException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import java.util.HashMap;
import java.util.Map;



// Controller class for main scene that is always visible.
public class TransactionTrackerController {
    @FXML private MenuBar menuBar;
    @FXML private Menu fileMenu;
    @FXML private MenuItem aboutMenuItem;
    @FXML private MenuItem addAccountMenuItem;
    @FXML private MenuItem addCategoryMenuItem;
    @FXML private MenuItem addItemMenuItem;
    @FXML private MenuItem editAbbreviationMenuItem;
    @FXML private MenuItem editAccountMenuItem;
    @FXML private MenuItem editCategoryMenuItem;
    @FXML private MenuItem editItemMenuItem;
    @FXML private MenuItem saveMenuItem;
    @FXML private MenuItem exitMenuItem;
    @FXML private ChoiceBox<String> categoryChoiceBox;
    @FXML private ChoiceBox<String> itemChoiceBox;
    @FXML private Button homeButton;
    @FXML private Button historyButton;
    @FXML private Button newBuyButton;
    @FXML private Button quickBuyButton;
    @FXML private Button investmentsButton;
    
    private ObservableList<String> choiceList = FXCollections.observableArrayList();
    private TransactionTracker transactionTracker;
    private buySceneController buyscenecontroller;
    
    
    @FXML
    public void initialize() {
        initializeChoiceBoxes();
        TransactionTracker.chosenItem = itemChoiceBox.getValue();
        
        // Listener for ChoiceBox, so can detect when new object is selected
        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsVal, String oldVal, String newVal) {
                changeCategories();
            } // changed
        }); // categoryChoiceBox Listener
        
        
        // Listener for ChoiceBox, so can keep track of currently selected item
        itemChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsVal, String oldVal, String newVal) {
                TransactionTracker.chosenItem = itemChoiceBox.getValue();
//                if (buyscenecontroller.onBuyScene == true)
//                    buyscenecontroller.refresh();
            } // changed
        }); // itemChoiceBox Listener
        
    } // initialize
    
    
    
    // Initializes the category choicebox [left] and item choicebox [right]. The item choicebox's
    // contents change according to what is selected by the category choicebox through the use of
    // Listeners. The categories / items are populated from Categories.txt.
    private void initializeChoiceBoxes() {
        choiceList.removeAll(choiceList);
        Object [] cats = TransactionTracker.categories.keySet().toArray();
        
        for (Object obj : cats) {
            if (!categoryChoiceBox.getItems().contains(obj.toString())) // to avoid duplicates
                choiceList.add(obj.toString());
        } // for
        
        categoryChoiceBox.getItems().addAll(choiceList);
        categoryChoiceBox.setValue(cats[0].toString());
        changeCategories(); // populate item choicebox based on what category is selected
    } // initializeChoiceBoxes
    
    
    
    // This function populates the item choicebox based on what category is selected from category
    // choicebox. Uses a hashmap to map the category to the item list
    private void changeCategories() {
        choiceList.removeAll(choiceList);
        itemChoiceBox.getItems().clear();
        
        for (int i = 0; i < TransactionTracker.categories.get(categoryChoiceBox.getValue()).size(); i++)
            choiceList.add(TransactionTracker.categories.get(categoryChoiceBox.getValue()).get(i));
        
        itemChoiceBox.getItems().addAll(choiceList);
        
        if (choiceList.size() > 0)
            itemChoiceBox.setValue(choiceList.get(0));
    } // changeCategories
    
    
    
    // When new buy button is pressed, the buy scene is shown and passed the currently
    // selected item from the item choice box so that it knows what the user is trying to
    // add a buy for
    @FXML
    void newBuyButtonPressed(ActionEvent event) throws IOException {
        if (itemChoiceBox.getValue() == null || itemChoiceBox.getValue().trim() == "")
            MessageBox.display("ERROR", "No item selected.\nPlease select an item.");
        else
            transactionTracker.showBuyScene(itemChoiceBox.getValue());
    } // newBuyButton
    
    
    // When history button is pressed, the history scene is shown. The history scene shows all
    // CompleteOffers that were read from completeOffers.txt
    @FXML
    void historyButtonPressed(ActionEvent event) throws IOException {
        transactionTracker.showHistoryScene();
    } // historyButtonPressed
    
    
    // When home button is pressed, the home scene is shown. The home scene shows ActiveOffers.
    // This is the default scene shown.
    @FXML
    void homeButtonPressed(ActionEvent event) throws IOException {
        transactionTracker.showHomeScene();
    } // homeButtonPressed
    
    
    @FXML
    void quickBuyButtonPressed(ActionEvent event) throws IOException {
        transactionTracker.showQuickBuyScene();
    } // quickBuyButtonPressed
    
    
    @FXML
    void investmentsButtonPressed(ActionEvent event) {
        
    } // investmentsButtonPressed
    
    
    // Uninitialized (ran out of time, apologies, will implement in future)
    @FXML
    void aboutMenuItemPressed(ActionEvent event) {
        MessageBox.display("About", "Information window");
    } // aboutMenuItemPressed
    
    @FXML
    void addAccountMenuItemPressed(ActionEvent event) {
        String acc = BasicAdd.display("account", TransactionTracker.accountList);
        if (!acc.equals("Cancel_SENTVAL"))
            TransactionTracker.accountList.add(acc);
    } // addAccountMenuItemPressed
    
    @FXML
    void addCategoryMenuItemPressed(ActionEvent event) {
        ArrayList<String> catAL = new ArrayList<String>();
        Object [] catArr = TransactionTracker.categories.keySet().toArray();
        
        for (Object obj : catArr)
            catAL.add(obj.toString());
        
        String cat = BasicAdd.display("category", catAL);
        
        if (!cat.equals("Cancel_SENTVAL")) {
            ArrayList<String> temp = new ArrayList<String>();
            TransactionTracker.categories.put(cat, temp);
            initializeChoiceBoxes();
        } // if
    } // addCategoryMenuItemPressed
    
    @FXML
    void addItemMenuButtonPressed(ActionEvent event) {
        String [] ans = AddItem.display(TransactionTracker.categories);
        
        if (!ans[0].equals("Cancel_SENTVAL")) {
            TransactionTracker.categories.get(ans[0]).add(ans[1]);
            TransactionTracker.itemDB.put(ans[1], ans[2]);
            
            String abbrev = ans[3];
            String [] tokens = ans[1].split(" ");
            
            // creates default abbreviations from first letter of each word
            if (abbrev == null || abbrev.equals("")) {
                for (String str : tokens) {
                    if (str.substring(0,1).equals("("))
                        abbrev += str.substring(1,2); // add number instead of '(' [ex. (6) for potion]
                    else
                        abbrev += str.substring(0,1);
                } // for
            } // if
            
            abbrev = abbrev.toLowerCase();
            
            // ensure no duplicate abbreviations
            while (TransactionTracker.abbreviations.containsKey(abbrev))
                abbrev += "1";
            
            TransactionTracker.abbreviations.put(abbrev, ans[1]); // abbrev -> name
            changeCategories();
        } // if
    } // addItemMenuButtonPressed
    
    
    @FXML
    void editAbbreviationMenuItemPressed(ActionEvent event) {
        
    } // editAbbreviationMenuItemPressed
    
    @FXML
    void editAccountMenuItemPressed(ActionEvent event) {
        
    } // editAccountMenuItemPressed
    
    
    
    @FXML
    void editCategoryMenuItemPressed(ActionEvent event) {
        String [] ans = EditCategory.display(TransactionTracker.categories);
        
        if (!ans[2].equals("Cancel_SENTVAL")) {
            if (ans[2].equals("Rename")) {
                TransactionTracker.categories.put(ans[1], TransactionTracker.categories.remove(ans[0]));
                categoryChoiceBox.getItems().remove(ans[0]);
                System.out.println(TransactionTracker.categories);
                initializeChoiceBoxes();
            }
            else if (ans[2].equals("Remove")) {
                TransactionTracker.categories.remove(ans[0]);
                categoryChoiceBox.getItems().remove(ans[0]);
                categoryChoiceBox.setValue(categoryChoiceBox.getItems().get(0));
            } // remove
        } // if
    } // editCategoryMenuItemPressed
    
    
    @FXML
    void editItemMenuItem(ActionEvent event) {
        String [] ans = EditItem.display(TransactionTracker.categories);
        
        if (!ans[3].equals("Cancel_SENTVAL")) {
            if (ans[3].equals("Move")) {
                TransactionTracker.categories.get(ans[0]).remove(ans[1]);
                TransactionTracker.categories.get(ans[2]).add(ans[1]);
                itemChoiceBox.getItems().remove(ans[1]);
                itemChoiceBox.setValue(itemChoiceBox.getItems().get(0));
                initializeChoiceBoxes();
            }
            else if (ans[3].equals("Remove")) {
                TransactionTracker.categories.get(ans[0]).remove(ans[1]);
                itemChoiceBox.getItems().remove(ans[1]);
                itemChoiceBox.setValue(itemChoiceBox.getItems().get(0));
                
                // remove abbreviation associated with item
                Object [] keys = TransactionTracker.abbreviations.keySet().toArray();
                
                for (int i = 0; i < TransactionTracker.abbreviations.size(); i++) {
                    if (TransactionTracker.abbreviations.get(keys[i]).equals(ans[1])) {
                        TransactionTracker.abbreviations.remove(keys[i]);
                        break;
                    } // if
                } // for
                
            } // remove
        } // if
    } // editItemMenuItem
    
    @FXML
    void saveMenuItemPressed(ActionEvent event) {
        
    } // saveMenuItemPressed
    
    
    @FXML
    void exitMenuItemPressed(ActionEvent event) {
//        TransactionTracker.closeProgram();
//        boolean result = ConfirmBox.display("Exit", "Are you sure you want to exit?");
//        if (result) {
//            Stage stage = (Stage) homeButton.getScene().getWindow();
//            stage.close();
//        }
    } // exitMenuItemPressed
    
    
} // TransactionTrackerController
