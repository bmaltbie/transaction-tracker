// Benjamin Maltbie - Final
// TransactionTracker Application Solution: homeSceneController.java
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.TableColumn;
import javafx.scene.control.SplitPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.collections.ListChangeListener;
import java.util.Arrays;
import javafx.beans.value.ObservableValue;
import java.util.List;
import javafx.beans.*;
import javafx.event.ActionEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.beans.property.StringProperty;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
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
import javafx.scene.control.Cell;
//import java.lang.NumberFormatException;


// Controller for home scene, this is the default scene that is display which shows ActiveOffers
// as well as a watch table. The watch table has yet to be implemented.
public class homeSceneController {
    
    @FXML private SplitPane mainSplitPane;
    @FXML private Label activeOffersLabel;
    @FXML private Label watchLabel;
    @FXML private TableView<Offer> activeOffersTable;
    @FXML private TableView<?> watchTable;
    @FXML private TableColumn<Offer, String> accountColumn;
    @FXML private TableColumn<Offer, Integer> quantityColumn;
    @FXML private TableColumn<Offer, String> itemNameColumn;
    @FXML private TableColumn<Offer, String> buyPriceColumn;
    @FXML private TableColumn<Offer, String> buyDateColumn;
    @FXML private TableColumn<Offer, Boolean> boughtColumn;
    @FXML private TableColumn<Offer, String> sellPriceColumn;
    
    private boolean initialized = false;
    
    @FXML
    public void initialize() {
        try {
            buySceneController.onBuyScene = false;
            initializeOfferTable();
        }
        catch (Exception e) {
            e.printStackTrace();
        } // catch
    } // initialize
    
    

    
    
    // Initializes activeOffersTable. Buy price, sell price, and bought can all be edited by the
    // user while the program is running. This allows the user to modify their transactions if
    // they make mistakes / update them before they are finalized and written to the file when
    // the program closes.
    @FXML
    public void initializeOfferTable() {
        activeOffersTable.setEditable(true);
        
        // Set up cellFactory
        Callback<TableColumn<Offer, String>, TableCell<Offer, String>> cellFactory
        = (TableColumn<Offer, String> param) -> new EditingCell();
        
        // Set up account, quantity, itemName, buyDate and bought columns
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        buyDateColumn.setCellValueFactory(new PropertyValueFactory<>("buyDate"));
        boughtColumn.setCellValueFactory(new PropertyValueFactory<>("bought"));
        
        
        // Set up buyPrice column, provides functionality to edit and commit edits in
        // TableView [TextField]
        buyPriceColumn.setCellValueFactory(cellData -> cellData.getValue().buyPriceProperty());
        buyPriceColumn.setCellFactory(cellFactory);
        buyPriceColumn.setOnEditCommit((TableColumn.CellEditEvent<Offer, String> t) -> {
            try {
                ((Offer) t
                 .getTableView()
                 .getItems()
                 .get(t.getTablePosition().getRow()))
                 .setBuyPrice(t.getNewValue());
            }
            catch (NumberFormatException e) {
                //e.printStackTrace();
                System.out.println("Caught!");
            } // catch
        }); // setOnEditCommit
        
        
        // Sell Price column, provides functionality to edit and commit edits in
        // TableView [TextField]
        sellPriceColumn.setCellValueFactory(cellData -> cellData.getValue().sellPriceProperty());
        sellPriceColumn.setCellFactory(cellFactory);
        sellPriceColumn.setOnEditCommit((TableColumn.CellEditEvent<Offer, String> t) -> {
            try {
                ((Offer) t
                 .getTableView()
                 .getItems()
                 .get(t.getTablePosition().getRow()))
                 .setSellPrice(t.getNewValue());
                
                if (t.getTableView().getItems().get(t.getTablePosition().getRow()).getBought() == true) {
                    // activeOffersTable.getItems().remove();
                    
                    Offer temp = (Offer) t.getTableView().getItems().get(t.getTablePosition().getRow());
                    TransactionTracker.activeList.remove(temp);
                    
                    Offer add = new Offer(temp.getAccount(), temp.getQuantity(), temp.getItemName(),
                                          temp.getBuyDate_date(), temp.getBuyPrice_str(),
                                          temp.getBought(), temp.getSellPrice_str(), LocalDateTime.now());
                    
                    TransactionTracker.completeList.add(0, add);
                } // if
            }
            catch (NumberFormatException e) {
                //e.printStackTrace();
            } // catch
        }); // setOnEditCommit
        
        
        // Bought column, provides functionality to edit and commit edits in TableView [CheckBox]
        boughtColumn.setCellValueFactory(new Callback<CellDataFeatures<Offer,Boolean>,
                                         ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<Offer, Boolean> param) {
                if (param.getValue().getBought() == true)
                    param.getValue().setBuyDate(LocalDateTime.now());
                
                return param.getValue().boughtProperty();
            } // call
        }); // setCellValueFactory
        boughtColumn.setCellFactory(CheckBoxTableCell.forTableColumn(boughtColumn));
        boughtColumn.setOnEditCommit((TableColumn.CellEditEvent<Offer, Boolean> t) -> {
            System.out.println("Edited");
        }); // setOnEditCommit
        
        
        // Set recentList items into activeOffersTable
        activeOffersTable.setItems(TransactionTracker.activeList);
        
    } // initializeOfferTable
    
} // homeSceneController





// This class is used to implement being able to edit TableView cells. When the user double clicks
// on a buy price or sell price cell, a textfield appears where they can edit/delete the existing
// data. The data is then saved into the objcet that those variables originally belonged to.
class EditingCell extends TableCell<Offer, String> {
    private TextField textField;
    
    public EditingCell() {
        
    } // EditingCell [default constructor]
    
    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        } // if
    } // startEdit
    
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        
        setText((String) getItem());
        setGraphic(null);
    } // cancelEdit
    
    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        
        if (empty) {
            setText(item);
            setGraphic(null);
        }
        else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                    // setGraphic(null);
                } // if
                
                setText(null);
                setGraphic(textField);
            }
            else {
                setText(getString());
                setGraphic(null);
            } // else
        } // else
    } // updateItem
    
    private void createTextField() {
        textField = new TextField(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnAction((e) -> commitEdit(textField.getText()));
        textField.focusedProperty().addListener(
            (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (!newValue) {
                    commitEdit(textField.getText());
                } // if
        }); // addListener
    } // createTextField
    
    private String getString() {
        return getItem() == null ? "" : getItem();
    } // getString
    
} // EditingCell


