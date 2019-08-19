// Benjamin Maltbie – Midterm 2
// TransactionTracker Application Solution: ActiveOffer.java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.CheckBox;
import java.lang.StringBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


// ActiveOffer class objects represent offers (buy/sell transactions) that have not been completed.
// When the transaction is completed, the ActiveOffer object is deleted and its information is
// transferred into a CompleteOffer object.
public class ActiveOffer {
    private String account;
    private int quantity;
    private String itemName;
    private LocalDateTime buyDate;
    private SimpleStringProperty buyPrice;
    private BooleanProperty bought;
    private SimpleStringProperty sellPrice;
    
    private int buyPrice_int;
    private int sellPrice_int;
    
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy h:mm a");
    
    
    // CONSTRUCTORS
    public ActiveOffer() {
        setAccount("Unknown");
        setQuantity(1);
        setItemName("Unknown item");
        setBuyDate(LocalDateTime.now());
        setBuyPrice("1");
        setBought(false);
        setSellPrice("1");
    } // ActiveOffer [default constructor]
    
    
    public ActiveOffer(String account, int quantity, String itemName, LocalDateTime buyDate,
                       String buyPrice, boolean bought, String sellPrice) {
        setAccount(account);
        setQuantity(quantity);
        setItemName(itemName);
        setBuyDate(buyDate);
        this.buyPrice = new SimpleStringProperty(buyPrice);
        setBuyPrice(buyPrice);
        this.bought = new SimpleBooleanProperty(bought);
        this.sellPrice = new SimpleStringProperty(sellPrice);
        setSellPrice(sellPrice);
    } // ActiveOffer [explicit constructor; int prices]
    

    

    // PROPERTIES
    public StringProperty buyPriceProperty() {
        SimpleStringProperty temp = new SimpleStringProperty(getBuyPrice());
        
        return temp; // return this.buyPrice;
    } // buyPriceProperty()
    
    
    public StringProperty sellPriceProperty() {
        SimpleStringProperty temp = new SimpleStringProperty(getSellPrice());
        
        return temp; // return this.sellPrice;
    } // sellPriceProperty
    
    
    public BooleanProperty boughtProperty() {
        return bought;
    } // boughtProperty
    
    
    
    
    // GET METHODS
    public String getAccount() {
        return account;
    } // getAccount
    
    public int getQuantity() {
        return quantity;
    } // getQuantity
    
    public String getItemName() {
        return itemName;
    } // getName
    
    public String getBuyDate() {
        String date = buyDate.format(formatter);
        
        return date;
    } // getBuyDate
    
    public LocalDateTime getBuyDate_date() {
        return buyDate;
    } // getBuyDate_date
    
    public LocalDateTime getBuyDate_Raw() {
        return buyDate;
    } // getBuyDate
    
    public String getBuyPrice() {
        return commaDelineate_String(buyPrice.get());
    } // getBuyPrice
    
    public String getBuyPrice_str() {
        return removeCommas(buyPrice.get());
    } // getBuyPrice_str
    
    public int getBuyPrice_int() {
        return buyPrice_int;
    } // getBuyPrice_int
    
    public boolean getBought() {
        return this.bought.get();
    } // getBought

    public String getSellPrice() {
        return commaDelineate_String(sellPrice.get());
    } // getSellPrice
    
    public String getSellPrice_str() {
        return removeCommas(sellPrice.get());
    } // getSellPrice_str
    
    public int getSellPrice_int() {
        return sellPrice_int;
    } // getSellPrice_int

    
    // Returns the date formatter used to read and write output
    public static String getFormatter() {
        return ("M/d/yy h:mm a");
    } // getFormatter
    
    // Returns the item name with underscores (_) instead of spaces, which is how they're read
    // from a file.
    public String getItemName_saveFormat() {
        return itemName.replace(" ", "_");
    } // getItemName_saveFormat
    

    
    
    // SET METHODS
    public void setAccount(String account) {
        this.account = account;
    } // setAccount
    
    
    public void setQuantity(int quantity) {
        if (quantity < 0 || quantity > Integer.MAX_VALUE)
            this.quantity = 1111; // sentinel value
        else
            this.quantity = quantity;
    } // setQuantity
    
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    } // setItemName
    
    
    public void setBuyDate(LocalDateTime buyDate) {
        this.buyDate = buyDate;
    } // setBuyDate
    
    
    public void setBuyPrice(String buyPrice) {
        int temp = Integer.parseInt(removeCommas(buyPrice));
        
        if (temp <= 0 || temp > Integer.MAX_VALUE) {
            this.buyPrice.set("1");
            this.buyPrice_int = 1;
        }
        else {
            this.buyPrice.set(String.valueOf(temp));
            this.buyPrice_int = temp;
        } // else
    } // setBuyPrice
    
    
    public void setBuyPrice_int(int buyPrice) {
        if (buyPrice <= 0 || buyPrice > Integer.MAX_VALUE) {
            this.buyPrice.set("1");
            this.buyPrice_int = 1;
        }
        else {
            this.buyPrice.set(String.valueOf(buyPrice));
            this.buyPrice_int = buyPrice;
        } // else
    } // setBuyPrice_int
    
    
    public void setBought(boolean bought) {
        this.bought.set(bought);
    } // setBought
    
    
    public void setSellPrice(String sellPrice) {
        int temp = Integer.parseInt(removeCommas(sellPrice));
        
        if (temp < 0 || temp > Integer.MAX_VALUE) {
            this.sellPrice.set("0");
            this.sellPrice_int = 0;
        }
        else {
            this.sellPrice.set(String.valueOf(temp));
            this.sellPrice_int = temp;
        } // else
    } // setSellPrice
    
    
    public void setSellPrice_int(int sellPrice) {
        if (sellPrice < 0 || sellPrice > Integer.MAX_VALUE) {
            this.sellPrice.set("0");
            this.sellPrice_int = 0;
        }
        else {
            this.sellPrice.set(String.valueOf(sellPrice));
            this.sellPrice_int = sellPrice;
        } // else
    } // setSellPrice_int
    
    
    
    
    
    
    // HELPER METHODS
    // This function takes in an int, turns it into a string and appropriately inserts commas
    private String commaDelineate_int(int str) {
        StringBuilder result = new StringBuilder(String.valueOf(str));
        
        for (int i = result.length()-3; i > 0; i -= 3) {
            result.insert(i, ",");
        } // for
        
        return (result.toString());
    } // commaDelineate_int
    
    
    // This function takes in a string and appropriately inserts commas
    private String commaDelineate_String(String str) {
        StringBuilder result = new StringBuilder(str);
        
        for (int i = result.length()-3; i > 0; i -= 3) {
            result.insert(i, ",");
        } // for
        
        return (result.toString());
    } // commaDelineate_String
    
    
    // This function takes in a string, removes commas, and returns the modified string
    private String removeCommas(String str) {
        return str.replace(",", "");
    } // removeCommas
    
    
    
    
    public String toString() {
        return (account + " " + quantity + " " + getItemName_saveFormat() + " " + getBuyDate()
                + " " + buyPrice.getValue() + " " + bought.getValue() + " " + sellPrice.getValue());
    } // toString
    
} // ActiveOffer
