// Benjamin Maltbie - Final
// TransactionTracker Application Solution: BasicAdd.java
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.util.List;


// Helper box for showing the pop-up window that appears when the user tries to add a new category
public class BasicAdd {
    
    static String answer;

    public static String display(String message, List<String> list) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); // blocks user interaction until popup closed
        window.setTitle("Add " + message);
        window.setMinWidth(280);
        window.setMinHeight(200);
        
        Label instructionLabel = new Label();
        instructionLabel.setText("Please enter a new " + message + " to add:");
        
        TextField inputTextField = new TextField();
        
        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");
        Insets instructionLabelInsets = new Insets(5, 0, 5, 0); // for instruction label
        instructionLabel.setPadding(instructionLabelInsets);
        
        Insets textFieldInsets = new Insets(0, 40, 0, 40); // for textField
        
        Label errorLabel = new Label();
        errorLabel.setText("");
        Insets errorLabelInsets = new Insets(10, 0, 30, 0); // for error label
        errorLabel.setPadding(errorLabelInsets);
        
        
        addButton.setOnAction(e -> {
            if (inputTextField.getText() == null || inputTextField.getText().trim().isEmpty())
                errorLabel.setText("Error: no " + message + " entered.");
            else if (list.contains(inputTextField.getText().trim())) {
                errorLabel.setText("Error: " + message + " already exists.");
            }
            else {
                answer = inputTextField.getText().trim();
                window.close();
            } // else
        }); // addButtonPressed
        
        
        cancelButton.setOnAction(e -> {
            answer = "Cancel_SENTVAL"; // sentinel value
            window.close();
        });// cancelButtonPressed
        
        window.setOnCloseRequest(e -> {
           answer = "Cancel_SENTVAL";
        }); // when user presses 'x' button instead of cancel button
        
        
        VBox layout = new VBox();
        HBox buttonMenu = new HBox();
        buttonMenu.getChildren().addAll(addButton, cancelButton);
        buttonMenu.setAlignment(Pos.CENTER);
        buttonMenu.setSpacing(20);
        
        layout.getChildren().addAll(instructionLabel, inputTextField, errorLabel, buttonMenu);
        layout.setMargin(inputTextField, textFieldInsets);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait(); // displays to the user and waits for it to be closed before returning to caller
    
        return answer;
    } // display

} // BasicAdd
