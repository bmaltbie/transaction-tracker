// Benjamin Maltbie – Final
// TransactionTracker Application Solution: EditItem.java
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


// Helper box for showing the pop-up window that appears when the user tries to edit items
public class EditItem {
    
    static Map<String, ArrayList<String>> hmap;
    static ObservableList<String> shapeList = FXCollections.observableArrayList();
    static String [] answer = new String[4]; // [category, item, toCategory, move/remove]
    static ChoiceBox<String> categoryChoiceBox = new ChoiceBox<>();
    static ChoiceBox<String> itemChoiceBox = new ChoiceBox<>();
    static ChoiceBox<String> moveChoiceBox = new ChoiceBox<>();

    public static String [] display(Map<String, ArrayList<String>> input) {
        hmap = input;
        
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); // blocks user interaction until popup closed
        window.setTitle("Edit Item");
        window.setMinWidth(380);
        window.setMinHeight(180);
        
        // Labels
        Label l1 = new Label("Category:");
        Insets l1Insets = new Insets(0, 0, 3, 2); // for l1 label
        l1.setPadding(l1Insets);
        Label l2 = new Label("Item:");
        Insets l2Insets = new Insets(8, 0, 3, 2); // for l2 label
        l2.setPadding(l2Insets);
        Label l3 = new Label("Move to:");
        Insets l3Insets = new Insets(0, 0, 3, 2); // for l3 label
        l3.setPadding(l3Insets);
        Label errorLabel = new Label();
        Insets errorLabelInsets = new Insets(25, 0, 0, 30); // for error label
        errorLabel.setPadding(errorLabelInsets);
        
        // Buttons
        Button moveButton = new Button("Move");
        Button removeButton = new Button("Remove");
        Button cancelButton = new Button("Cancel");
        
        // ChoiceBoxes
        categoryChoiceBox.setMinWidth(150);
        itemChoiceBox.setMinWidth(150);
        moveChoiceBox.setMinWidth(145);
        
        moveChoiceBox.getItems().clear();
        initializeChoiceBoxes();
        
        // Listener for ChoiceBox, so can detect when new object is selected
        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsVal, String oldVal, String newVal) {
                changeCategories();
            } // changed
        }); // categoryChoiceBox Listener
        
        
        // Button actions
        moveButton.setOnAction(e -> {
            if (categoryChoiceBox.getValue().equals(moveChoiceBox.getValue()))
                errorLabel.setText("Error: already \nin category");
            else {
                answer[0] = categoryChoiceBox.getValue();
                answer[1] = itemChoiceBox.getValue();
                answer[2] = moveChoiceBox.getValue();
                answer[3] = "Move";
                window.close();
            } // else
        }); // moveButtonPressed
        
        removeButton.setOnAction(e -> {
            boolean result = ConfirmBox.display("Remove", "Are you sure you want to remove?");
            
            if (result) {
                answer[0] = categoryChoiceBox.getValue();
                answer[1] = itemChoiceBox.getValue();
                answer[2] = "";
                answer[3] = "Remove";
                window.close();
            } // if
        }); // removeButtonPressed
        
        
        cancelButton.setOnAction(e -> {
            answer[3] = "Cancel_SENTVAL";
            window.close();
        });// cancelButtonPressed
        
        
        window.setOnCloseRequest(e -> {
            answer[3] = "Cancel_SENTVAL";
        }); // when user presses 'x' button instead of cancel button


        BorderPane mainLayout = new BorderPane();
        HBox HBLayoutTOP = new HBox();
        VBox VBLayoutLEFT = new VBox();
        VBox VBLayoutRIGHT = new VBox();
        HBox buttonMenu = new HBox();
        
        VBLayoutLEFT.setMinWidth(205);
        VBLayoutLEFT.setMinHeight(130);
        Insets VBLEFTInsets = new Insets(15, 0, 0, 25);
        VBLayoutLEFT.setPadding(VBLEFTInsets);

        VBLayoutRIGHT.setMinWidth(175);
        VBLayoutRIGHT.setMinHeight(130);
        Insets VBRIGHTInsets = new Insets(15, 20, 0, 5);
        VBLayoutRIGHT.setPadding(VBRIGHTInsets);
    
        VBLayoutLEFT.getChildren().addAll(l1, categoryChoiceBox, l2, itemChoiceBox);
        VBLayoutRIGHT.getChildren().addAll(l3, moveChoiceBox, errorLabel);
        
        HBLayoutTOP.setMinHeight(130);
        HBLayoutTOP.getChildren().addAll(VBLayoutLEFT, VBLayoutRIGHT);
        buttonMenu.setMinHeight(50);
        buttonMenu.getChildren().addAll(moveButton, removeButton, cancelButton);
        buttonMenu.setAlignment(Pos.CENTER);
        buttonMenu.setSpacing(20);
        Insets buttonMenuInsets = new Insets(0, 0, 5, 0);
        buttonMenu.setPadding(buttonMenuInsets);
        
        mainLayout.setCenter(HBLayoutTOP);
        mainLayout.setBottom(buttonMenu);
        
        Scene scene = new Scene(mainLayout);
        window.setScene(scene);
        window.showAndWait(); // displays to the user and waits for it to be closed before returning to caller
    
        return answer;
    } // display
    
    
    // Initializes the category choicebox [left] and item choicebox [right]. The item choicebox's
    // contents change according to what is selected by the category choicebox through the use of
    // Listeners. The categories / items are populated from Categories.txt.
    public static void initializeChoiceBoxes() {
        shapeList.removeAll(shapeList);
        Object [] cats = hmap.keySet().toArray();
        
        for (Object obj : cats) {
            if (!categoryChoiceBox.getItems().contains(obj.toString())) // to avoid duplicates
                shapeList.add(obj.toString());
        } // for
        
        categoryChoiceBox.getItems().addAll(shapeList);
        categoryChoiceBox.setValue(cats[0].toString());
        
        moveChoiceBox.getItems().addAll(categoryChoiceBox.getItems());
        moveChoiceBox.setValue(cats[0].toString());
        
        changeCategories(); // populate item choicebox based on what category is selected
    } // initializeChoiceBoxes
    
    
    
    // This function populates the item choicebox based on what category is selected from category
    // choicebox. Uses a hashmap to map the category to the item list
    public static void changeCategories() {
        shapeList.removeAll(shapeList);
        itemChoiceBox.getItems().clear();
        
        for (int i = 0; i < hmap.get(categoryChoiceBox.getValue()).size(); i++)
            shapeList.add(hmap.get(categoryChoiceBox.getValue()).get(i));
        
        itemChoiceBox.getItems().addAll(shapeList);
        
        if (shapeList.size() > 0)
            itemChoiceBox.setValue(shapeList.get(0));
    } // changeCategories
    
} // EditItem



