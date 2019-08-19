// Benjamin Maltbie - Final
// TransactionTracker Application Solution: WebBrowser.java
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


// Browser used in 'Add buy' to display the related RSWiki/RS GE pages
public class WebBrowser {
    
    static String answer;

    public static void display(String URL) {
        Stage window = new Stage();
        window.setTitle("Web Browser");
        window.setMinWidth(300);
        window.setMinHeight(700);
        
        TextField urlTextField = new TextField();
        Button goButton = new Button("GO");
        WebView webView = new WebView();
        WebEngine webEngine = new WebEngine();
        
        webEngine = webView.getEngine();
        webEngine.load(URL);
    
        VBox layout = new VBox();
        HBox menu = new HBox();
        
        layout.getChildren().addAll(webView);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait(); // displays to the user and waits for it to be closed before returning to caller
    } // display

} // WebBrowser
