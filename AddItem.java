// Benjamin Maltbie - Final
// TransactionTracker Application Solution: AddItem.java
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;


// Helper box for showing the pop-up window that appears when the user tries to add a new item
public class AddItem {
    
    static String [] answer = new String[4];

    public static String [] display(Map<String, ArrayList<String>> hmap) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); // blocks user interaction until popup closed
        window.setTitle("Add Item");
        window.setMinWidth(280);
        window.setMinHeight(280); // 240
        
        Label l1 = new Label();
        l1.setText("Category:");
        Insets catLabelInsets = new Insets(15, 0, 5, 0); // for category label
        l1.setPadding(catLabelInsets);
        
        Label instructionLabel = new Label();
        instructionLabel.setText("Please enter an item ID:");
        Insets instructionLabelInsets = new Insets(15, 0, 5, 0); // for instruction label
        instructionLabel.setPadding(instructionLabelInsets);
        
        TextField inputTextField = new TextField();
        Insets textFieldInsets = new Insets(0, 40, 0, 40); // for input textField
        
        TextField abbrevTextField = new TextField();
        Insets abbrevInsets = new Insets(10, 60, 0, 60); // for abbrev textField
        abbrevTextField.setPromptText("Abbreviation (optional)");
        
        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");
        
        Label errorLabel = new Label();
        errorLabel.setText("");
        Insets errorLabelInsets = new Insets(10, 0, 30, 0); // for error label
        errorLabel.setPadding(errorLabelInsets);
        
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        for (Object obj : hmap.keySet().toArray())
            choiceBox.getItems().add(obj.toString());
        choiceBox.setValue(hmap.keySet().toArray()[0].toString());
        
        
        addButton.setOnAction(e -> {
            if (inputTextField.getText() == null || inputTextField.getText().trim().isEmpty())
                errorLabel.setText("Error: no item entered.");
            else {
                String [] temp = buySceneController.getItemData(inputTextField.getText().trim());
                
                if (temp[0].equals("Failed")) {
                    errorLabel.setText("Error: invalid item ID entered.");
                }
                else if (hmap.get(choiceBox.getValue()).contains(temp[0])) {
                    errorLabel.setText("Error: item already exists.");
                }
                else {
                    answer[0] = choiceBox.getValue(); // category
                    answer[1] = temp[0]; // name of item
                    answer[2] = inputTextField.getText().trim(); // itemID
                    answer[3] = abbrevTextField.getText().trim(); // user specified abbreviation
                    window.close();
                } // else
            } // else
        }); // addButtonPressed
        
        
        cancelButton.setOnAction(e -> {
            answer[0] = "Cancel_SENTVAL";
            window.close();
        });// cancelButtonPressed
        
        window.setOnCloseRequest(e -> {
            answer[0] = "Cancel_SENTVAL";
        }); // when user presses 'x' button instead of cancel button

        
        
        
        VBox layout = new VBox();
        HBox buttonMenu = new HBox();
        buttonMenu.getChildren().addAll(addButton, cancelButton);
        buttonMenu.setAlignment(Pos.CENTER);
        buttonMenu.setSpacing(20);
        Insets buttonMenuInsets = new Insets(0, 0, 15, 0);
        buttonMenu.setPadding(buttonMenuInsets);
        
        layout.getChildren().addAll(l1, choiceBox, instructionLabel, inputTextField,
                                    abbrevTextField, errorLabel, buttonMenu);
        layout.setMargin(inputTextField, textFieldInsets);
        layout.setMargin(abbrevTextField, abbrevInsets);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait(); // displays to the user and waits for it to be closed before returning to caller
    
        return answer;
    } // display

} // AddItem
