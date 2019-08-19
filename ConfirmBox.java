// Benjamin Maltbie - Final
// TransactionTracker Application Solution: ConfirmBox.java
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;


// Helper box for showing the pop-up window that appears when the user tries to close the program
public class ConfirmBox {
    
    static boolean answer;

    public static boolean display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); // blocks user interaction until popup closed
        window.setTitle(title);
        window.setMinWidth(250);
        
        Label label = new Label();
        label.setText(message);
        
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");
        
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });
        
        VBox layout = new VBox(10);
        Insets insets = new Insets(0, 0, 10, 0);
        layout.setPadding(insets);
        HBox buttonMenu = new HBox();
        buttonMenu.getChildren().addAll(yesButton, noButton);
        buttonMenu.setAlignment(Pos.CENTER);
        buttonMenu.setSpacing(20);
        
        layout.getChildren().addAll(label, buttonMenu);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait(); // displays to the user and waits for it to be closed before returning to caller
    
        return answer;
    } // display

} // ConfirmBox
