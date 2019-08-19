// Benjamin Maltbie – Midterm 2
// TransactionTracker Application Solution: CompleteOffer.java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.CheckBox;
import java.lang.StringBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


// CompleteOffer objects represent offers (buy/sell transaction) that have been completed. They
// differ from ActiveOffer objects in that they have sellDate & profit class variables.
public class CompleteOffer { // implements Comparable<CompleteOffer>
    private String account;
    private int quantity;
    private String itemName;
    private LocalDateTime buyDate;
    private SimpleStringProperty buyPrice;
    private BooleanProperty bought;
    private SimpleStringProperty sellPrice;
    private LocalDateTime sellDate;
    private long profit;
    
    private int buyPrice_int;
    private int sellPrice_int;
    
    private DateTimeFormatter stdFormatter = DateTimeFormatter.ofPattern("M/d/yy h:mm a");
    
    
    // CONSTRUCTORS
    public CompleteOffer() {
        setAccount("Unknown");
        setQuantity(1);
        setItemName("Unknown item");
        setBuyDate(LocalDateTime.now());
        setBuyPrice("1");
        setBought(false);
        setSellPrice("1");
        setSellDate(LocalDateTime.now());
        setProfit(0);
    } // CompleteOffer [default constructor]
    
    
    public CompleteOffer(String account, int quantity, String itemName, LocalDateTime buyDate,
                       String buyPrice, boolean bought, String sellPrice,
                         LocalDateTime sellDate, long profit) {
        setAccount(account);
        setQuantity(quantity);
        setItemName(itemName);
        setBuyDate(buyDate);
        this.buyPrice = new SimpleStringProperty(buyPrice);
        setBuyPrice(buyPrice);
        this.bought = new SimpleBooleanProperty(bought);
        this.sellPrice = new SimpleStringProperty(sellPrice);
        setSellPrice(sellPrice);
        setSellDate(sellDate);
        setProfit(profit);
    } // CompleteOffer [explicit constructor; int prices]
    

    

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
        String date = buyDate.format(stdFormatter);
        
        return date;
    } // getBuyDate
    
    public LocalDateTime getBuyDate_date() {
        return buyDate;
    } // getBuyDate_date
    
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
    
    public String getSellDate() {
        String date = sellDate.format(stdFormatter);
        
        return date;
    } // getSellDate
    
    public String getProfit() {
        return commaDelineate_long(profit);
    } // getProfit
    
    public long getProfit_long() {
        return profit;
    } // getProfit_long
    

    // This function prints out a formatted date range. If buy and sell were both today, then
    // prints the range as times [ex. 9:00 am - 6:00 pm]. If buy and sell were before today,
    // and not on the same days, then it will print the range as dates but no times
    // [ex. 3/11/19 - 3/12/19]. If buy and sell were before today and on the same day, then it
    // will print the range as that single date [ex. 3/11/19].
    public String getDateRange() {
        String buy, sell, dateRange = "";
        DateTimeFormatter hour_min = DateTimeFormatter.ofPattern("h:mm a");
        DateTimeFormatter mon_day_year = DateTimeFormatter.ofPattern("M/d/yy");
        
        
        int hourDifference = LocalDateTime.now().getHour() - buyDate.getHour();
        int minuteDifference = LocalDateTime.now().getMinute() - buyDate.getMinute();
        LocalDateTime temp = buyDate.plusHours(hourDifference);
        temp = temp.plusMinutes(minuteDifference+1);
        
        // Buy and sell dates were today
        if (temp.compareTo(LocalDateTime.now()) > -1) {
            buy = buyDate.format(hour_min);
            sell = sellDate.format(hour_min);
            dateRange = buy + " - " + sell;
        }
        else if (buyDate.getDayOfMonth() == sellDate.getDayOfMonth()) {
            dateRange = buyDate.format(mon_day_year);
        }
        else {
            buy = buyDate.format(mon_day_year);
            sell = sellDate.format(mon_day_year);
            dateRange = buy + " - " + sell;
        } // else
        
        return dateRange;
    } // getDateRange

    
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
    
    
    public void setSellDate(LocalDateTime sellDate) {
        this.sellDate = sellDate;
    } // setSellDate
    

    public void setProfit(long profit) {
//        if (profit < 0 || profit > Integer.MAX_VALUE)
//            this.profit = 0;
//        else
//
        this.profit = profit;
    } // setProfit
    
    
    public void setProfit_String(String profit) {
        setProfit(Long.parseLong(removeCommas(profit)));
    } // setProfit_String
    
    
    
    
    // HELPER METHODS
    // This function takes in an int, turns it into a string and appropriately inserts commas
    private String commaDelineate_int(int str) {
        StringBuilder result = new StringBuilder(String.valueOf(str));
        
        for (int i = result.length()-3; i > 0; i -= 3) {
            result.insert(i, ",");
        } // for
        
        return (result.toString());
    } // commaDelineate_int
    
    
    private String commaDelineate_long(long str) {
        StringBuilder result = new StringBuilder(String.valueOf(str));
        int limit = 0;
        if (str < 0)
            limit = 1;
        
        for (int i = result.length()-3; i > limit; i -= 3) {
            result.insert(i, ",");
        } // for
        
        return (result.toString());
    } // commaDelineate_long
    
    
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
    
    
//    public int compareTo(CompleteOffer obj) {
//        return buyDate.compareTo(obj.getBuyDate_date());
//    } // compareTo
    
    
    
    public String toString() {
        return (account + " " + quantity + " " + getItemName_saveFormat() + " " + getBuyDate()
                + " " + buyPrice.getValue() + " " + bought.getValue() + " " + sellPrice.getValue()
                + " " + getSellDate() + " " + profit);
    } // toString
    
} // CompleteOffer
