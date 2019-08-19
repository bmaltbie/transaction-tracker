// Benjamin Maltbie - Final
// TransactionTracker Application Solution: historySceneController.java
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import javafx.scene.control.Separator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


// Controller for history scene, accessed by pressing history button. This scene shows all completed
// offers and calculates the profit earned by all of them. The user is able to select if they want
// to view all items or if they want to view a custom range of items (entered by typing in the name
// of the item they want to see) – this is controlled by radio buttons. They can also further
// narrow down results by selecting which time period the transaction took place with a choice box.
// There are listeners to both of the radio buttons and the choice box so that the history table
// can dynamically update
public class historySceneController {
    @FXML private RadioButton viewAllRadioButton;
    @FXML private RadioButton viewCustomRadioButton;
    @FXML private ToggleGroup view;
    @FXML private TextField viewCustomTextField;
    @FXML private ChoiceBox<String> profitChoiceBox;
    @FXML private Label profitLabel;
    @FXML private TableView<Offer> historyTable;
    @FXML private TableColumn<Offer, String> accountColumn;
    @FXML private TableColumn<Offer, String> dateColumn;
    @FXML private TableColumn<Offer, Integer> quantityColumn;
    @FXML private TableColumn<Offer, String> itemColumn;
    @FXML private TableColumn<Offer, String> buyPriceColumn;
    @FXML private TableColumn<Offer, String> sellPriceColumn;
    @FXML private TableColumn<Offer, String> profitColumn;
    
    private ObservableList<Offer> historyList = FXCollections.observableArrayList();
    private Map<String, Integer> months = new HashMap<String, Integer>(); // month -> month #
    private ObservableList<String> choiceList = FXCollections.observableArrayList();
    public static long totalProfit = 0;

    private long totProfit = 0;
    
    public void initialize() {
        // To ensure that add buy reinitializes the recent offer table according to new item
        TransactionTracker.recentInitialized = false;
        buySceneController.onBuyScene = false;
        initializeProfitChoiceBox();
        
        viewAllRadioButton.setUserData("View All");
        viewCustomRadioButton.setUserData("View Custom");
        initializeHistoryTable(""); // initialize table
        
        
        // Listeners
        view.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (view.getSelectedToggle() != null) {
                    if (view.getSelectedToggle().getUserData().toString().equals("View Custom"))
                        viewCustomTextField.setDisable(false);
                    else {
                        viewCustomTextField.setText("");
                        viewCustomTextField.setDisable(true);
                    } // else
                    initializeHistoryTable(viewCustomTextField.getText());
                    
                } // if
            } // changed
        }); // Toggle listener
        
        profitChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsVal, String oldVal, String newVal) {
                initializeHistoryTable(viewCustomTextField.getText());
            } // changed
        }); // profitChoiceBox Listener
        
        viewCustomTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            initializeHistoryTable(newValue);
        }); // customTextField listener
        
    } // initialize
    
    
    
    // Initializes History Table with completed Offer objects
    private void initializeHistoryTable(String search) {
        historyTable.getItems().clear(); // clear existing data
        totProfit = 0;
        
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateRange"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        buyPriceColumn.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
        sellPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        profitColumn.setCellValueFactory(new PropertyValueFactory<>("profit"));
        // historyList
        
        
        if (view.getSelectedToggle().getUserData().toString().equals("View All") &&
            profitChoiceBox.getValue().equals("All Time")) { // all items, all time
            
            for (int i = 0; i < TransactionTracker.completeList.size(); i++)
                historyTable.getItems().add(new Offer(TransactionTracker.completeList.get(i)));
            
            //historyTable.setItems(TransactionTracker.completeList);
        }
        else if (!profitChoiceBox.getValue().equals("All Time")) { // view specific time
            boolean validTime = false;
            for (int i = 0; i < TransactionTracker.completeList.size(); i++) {
                
                // if today or specific month
                if (profitChoiceBox.getValue().equals("Today")) {
                    if (TransactionTracker.completeList.get(i).getBuyDate_date().getDayOfMonth() == LocalDateTime.now().getDayOfMonth() &&
                        TransactionTracker.completeList.get(i).getBuyDate_date().getMonthValue() == LocalDateTime.now().getMonthValue() &&
                        TransactionTracker.completeList.get(i).getBuyDate_date().getYear() == LocalDateTime.now().getYear())
                        validTime = true;
                }
                else {
                    int searchMonth = months.get(profitChoiceBox.getValue());
                
                    if (TransactionTracker.completeList.get(i).getBuyDate_date().getMonthValue() == searchMonth &&
                        TransactionTracker.completeList.get(i).getBuyDate_date().getYear() == LocalDateTime.now().getYear())
                            validTime = true;
                } // else
                
                // custom search or all items
                if (validTime) {
                    if (!view.getSelectedToggle().getUserData().toString().equals("View All")) {
                        if (TransactionTracker.completeList.get(i).getItemName().toLowerCase().contains(search.toLowerCase()))
                            historyTable.getItems().add(new Offer(TransactionTracker.completeList.get(i)));
                    }
                    else // all items
                        historyTable.getItems().add(new Offer(TransactionTracker.completeList.get(i)));
                }
//                else
//                    break; // since ordered sequentially by date, if not valid, we passed point where it could be valid
                
                validTime = false;
            } // for
        }
        else {
            // specific item, all time
            for (int i = 0; i < TransactionTracker.completeList.size(); i++) {
                if (TransactionTracker.completeList.get(i).getItemName().toLowerCase().contains(search.toLowerCase()))
                    historyTable.getItems().add(new Offer(TransactionTracker.completeList.get(i)));
            } // for
        } // else
        
        
        
        for (int i = 0; i < historyTable.getItems().size(); i++)
            totProfit += historyTable.getItems().get(i).getProfit_long();
        
        profitLabel.setText(commaDelineate_String(String.valueOf(totProfit)));
    
        
    } // initializeHistoryTable
    
    
    // Based on items that are displayed in the history table, show their cumulative profit
    private void initializeProfitChoiceBox() {
        choiceList.removeAll(choiceList);
        choiceList.addAll("January", "February", "March", "April", "May", "June", "July",
                         "August", "September", "October", "November", "December");
        
        for (int i = 0; i < choiceList.size(); i++) {
            months.put(choiceList.get(i).toString(), i+1);
        } // for
        
        
        // choiceList.addAll(Arrays.asList(months.keySet().toArray(new String[0])));
        choiceList.add(0, "All Time");
        choiceList.add(1, "Today");
        // choiceList.add(2, new Separator());
        
        profitChoiceBox.getItems().addAll(choiceList);
        // profitChoiceBox.getItems().add(2, new Separator());
        profitChoiceBox.setValue(choiceList.get(0));
    } // initializeProfitChoiceBox
    
    
    // Helper function to make prices easier to read by inserting commas.
    private String commaDelineate_String(String str) {
        StringBuilder result = new StringBuilder(str);
        int limit = 0;
        if (str.substring(0,1).equals("-"))
            limit = 1;
        
        for (int i = result.length()-3; i > limit; i -= 3) {
            result.insert(i, ",");
        } // for
        
        return (result.toString());
    } // commaDelineate_String
    
    
} // historySceneController
