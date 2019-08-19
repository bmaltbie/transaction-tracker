// Benjamin Maltbie – Final
// TransactionTracker Application Solution: EditCategory.java
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;


// Helper box for showing the pop-up window that appears when the user tries to edit a categories
public class EditCategory {
    
    static String [] answer = new String[3]; // [category, name, remove/rename]

    public static String [] display(Map<String, ArrayList<String>> hmap) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); // blocks user interaction until popup closed
        window.setTitle("Edit Category");
        window.setMinWidth(260);
        window.setMinHeight(250);
        
        Label l1 = new Label();
        l1.setText("Category:");
        Insets catLabelInsets = new Insets(10, 0, 5, 0); // for category label
        l1.setPadding(catLabelInsets);
        
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        for (Object obj : hmap.keySet().toArray())
            choiceBox.getItems().add(obj.toString());
        choiceBox.setValue(hmap.keySet().toArray()[0].toString());
        
        Label instructionLabel = new Label();
        instructionLabel.setText("Change name to:");
        Insets instructionLabelInsets = new Insets(15, 0, 5, 0); // for instruction label
        instructionLabel.setPadding(instructionLabelInsets);
        
        TextField inputTextField = new TextField();
        Insets textFieldInsets = new Insets(0, 40, 0, 40); // for textField
        
        Button renameButton = new Button("Rename");
        Button removeButton = new Button("Remove");
        Button cancelButton = new Button("Cancel");
        
        Label errorLabel = new Label();
        errorLabel.setText("");
        Insets errorLabelInsets = new Insets(10, 0, 20, 0); // for error label
        errorLabel.setPadding(errorLabelInsets);
        
        
        renameButton.setOnAction(e -> {
            if (inputTextField.getText() == null || inputTextField.getText().trim().isEmpty())
                errorLabel.setText("Error: name can't be empty");
            else if (hmap.containsKey(inputTextField.getText().trim())) {
                errorLabel.setText("Error: category already exists.");
            }
            else {
                answer[0] = choiceBox.getValue();
                answer[1] = inputTextField.getText().trim();
                answer[2] = "Rename";
                window.close();
            } // else
        }); // renameButtonPressed
        
        removeButton.setOnAction(e -> {
            boolean result = ConfirmBox.display("Remove", "Are you sure you want to remove?");
            
            if (result) {
                answer[0] = choiceBox.getValue();
                answer[1] = "";
                answer[2] = "Remove";
                window.close();
            } // if
        }); // removeButtonPressed
        
        
        cancelButton.setOnAction(e -> {
            answer[2] = "Cancel_SENTVAL";
            window.close();
        });// cancelButtonPressed
        
        window.setOnCloseRequest(e -> {
            answer[2] = "Cancel_SENTVAL";
        }); // when user presses 'x' button instead of cancel button

        
        VBox layout = new VBox();
        HBox buttonMenu = new HBox();
        buttonMenu.getChildren().addAll(renameButton, removeButton, cancelButton);
        buttonMenu.setAlignment(Pos.CENTER);
        buttonMenu.setSpacing(10);
        Insets buttonMenuInsets = new Insets(0, 0, 10, 0);
        buttonMenu.setPadding(buttonMenuInsets);
        
        layout.getChildren().addAll(l1, choiceBox, instructionLabel, inputTextField, errorLabel, buttonMenu);
        layout.setMargin(inputTextField, textFieldInsets);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait(); // displays to the user and waits for it to be closed before returning to caller
    
        return answer;
    } // display

} // EditCategory
