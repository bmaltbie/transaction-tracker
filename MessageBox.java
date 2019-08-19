// Benjamin Maltbie - Final
// TransactionTracker Application Solution: MessageBox.java
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;


// Helper box for showing the pop-up window that appears when an error occurs
public class MessageBox {
    
    public static void display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); // blocks user interaction until popup closed
        window.setTitle(title);
        window.setMinWidth(250);
        
        Label label = new Label();
        label.setText(message);
        
        Button okButton = new Button("OK");
        
        okButton.setOnAction(e -> {
            window.close();
        });
        
        VBox layout = new VBox(10);
        Insets insets = new Insets(10, 0, 10, 0);
        layout.setPadding(insets);
        
        layout.getChildren().addAll(label, okButton);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait(); // displays to the user and waits for it to be closed before returning to caller
    } // display

} // MessageBox
