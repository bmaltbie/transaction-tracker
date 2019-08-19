// Benjamin Maltbie - Final
// TransactionTracker Application Solution: Offer.java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.CheckBox;
import java.lang.StringBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;



// Offer object represent offers (buy/sell transaction) that are active or have been ompleted. They
// differ from ActiveOffer objects in that they have sellDate & profit class variables.
public class Offer {
    private String account;
    private int quantity;
    private String itemName;
    private LocalDateTime buyDate;
    private SimpleStringProperty buyPrice;
    private BooleanProperty bought;
    private SimpleStringProperty sellPrice;
    private LocalDateTime sellDate;
    private long profit;
    
    private long buyPrice_long;
    private long sellPrice_long;
    private DateTimeFormatter stdFormatter = DateTimeFormatter.ofPattern("M/d/yy h:mm a");
    
    
    // CONSTRUCTORS
    public Offer() {
        setAccount("Unknown");
        setQuantity(1);
        setItemName("Unknown item");
        setBuyDate(LocalDateTime.now());
        setBuyPrice("1");
        setBought(false);
        setSellPrice("1");
        setSellDate(LocalDateTime.now());
        setProfit(0);
    } // Offer [default constructor]
    
    
    public Offer(Offer off) {
        setAccount(off.getAccount());
        setQuantity(off.getQuantity());
        setItemName(off.getItemName());
        setBuyDate(off.getBuyDate_date());
        this.buyPrice = new SimpleStringProperty(off.getBuyPrice_str());
        setBuyPrice(off.getBuyPrice_str());
        this.bought = new SimpleBooleanProperty(off.getBought());
        this.sellPrice = new SimpleStringProperty(off.getSellPrice_str());
        setBought(off.getBought());
        setSellPrice(off.getSellPrice());
        setSellDate(off.getSellDate_date());
        setProfit(off.getProfit_long());
    } // Offer [explicit constructor; copy constructor]
    
    
    public Offer(String account, int quantity, String itemName, LocalDateTime buyDate,
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
        setSellDate(LocalDateTime.now());
        setProfit(0);
    } // Offer [explicit constructor; acts as ActiveOffer]

    
    public Offer(String account, int quantity, String itemName, LocalDateTime buyDate,
                 String buyPrice, boolean bought, String sellPrice, LocalDateTime sellDate) {
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
        setProfit(quantity*(sellPrice_long-buyPrice_long));
    } // Offer [explicit constructor; for converting ActiveOffer to CompleteOffer]
    
    
    public Offer(String account, int quantity, String itemName, LocalDateTime buyDate,
                 long buyPrice, boolean bought, long sellPrice, LocalDateTime sellDate) {
        setAccount(account);
        setQuantity(quantity);
        setItemName(itemName);
        setBuyDate(buyDate);
        this.buyPrice = new SimpleStringProperty(String.valueOf(buyPrice));
        setBuyPrice_long(buyPrice);
        this.bought = new SimpleBooleanProperty(bought);
        this.sellPrice = new SimpleStringProperty(String.valueOf(sellPrice));
        setSellPrice_long(sellPrice);
        setSellDate(sellDate);
        setProfit(quantity*(sellPrice_long-buyPrice_long));
    } // Offer [explicit constructor; for use in quickBuy]
    
    
    
    public Offer(String account, int quantity, String itemName, LocalDateTime buyDate,
                 String buyPrice, boolean bought, String sellPrice, LocalDateTime sellDate,
                 long profit) {
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
    } // Offer [explicit constructor; long prices, acts as CompleteOffer]
    

    

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
    
    // Returns the item name with underscores (_) instead of spaces, which is how they're read
    // from a file.
    public String getItemName_saveFormat() {
        return itemName.replace(" ", "_");
    } // getItemName_saveFormat
    
    public String getBuyDate() {
        return (buyDate.format(stdFormatter)); // formatted date string
    } // getBuyDate
    
    public LocalDateTime getBuyDate_date() {
        return buyDate;
    } // getBuyDate_date
    
    public String getBuyPrice() {
        return commaDelineate_String(buyPrice.get()); // comma delimited string
    } // getBuyPrice
    
    public String getBuyPrice_str() {
        return removeCommas(buyPrice.get()); // no comma string
    } // getBuyPrice_str
    
    public long getBuyPrice_long() {
        return buyPrice_long; // long
    } // getBuyPrice_long
    
    public boolean getBought() {
        return this.bought.get(); // boolean value instead of boolean property
    } // getBought

    public String getSellPrice() {
        return commaDelineate_String(sellPrice.get()); // comma delimited string
    } // getSellPrice
    
    public String getSellPrice_str() {
        return removeCommas(sellPrice.get()); // no comma string
    } // getSellPrice_str
    
    public long getSellPrice_long() {
        return sellPrice_long; // long
    } // getSellPrice_long
    
    public String getSellDate() {
        return (sellDate.format(stdFormatter)); // formatted date string
    } // getSellDate
    
    public LocalDateTime getSellDate_date() {
        return sellDate;
    } // getSellDate_date
    
    public String getProfit() {
        return commaDelineate_long(profit); // comma delimited string
    } // getProfit
    
    public long getProfit_long() {
        return profit; // long
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
    
    
    // Price is stored as long in case in the future we can accept values greater than MAX_INT
    // Has data validation to make sure it currently falls in bounds of [0, MAX_INT]
    public void setBuyPrice(String buyPrice) {
        String tempStr = removeCommas(buyPrice);
        double tempDouble;
        long tempLong;
        
        // Allow for 'b', 'm' and 'k' shortcuts in price
        if (tempStr.substring(tempStr.length()-1, tempStr.length()).toLowerCase().equals("b")) {
            tempStr = tempStr.substring(0, tempStr.length()-1);
            tempDouble = Double.parseDouble(tempStr);
            tempLong = (long)(tempDouble*1000000000);
            // tempLong *= 1000000000;
        }
        else if (tempStr.substring(tempStr.length()-1, tempStr.length()).toLowerCase().equals("m")) {
            tempStr = tempStr.substring(0, tempStr.length()-1);
            tempDouble = Double.parseDouble(tempStr);
            tempLong = (long)(tempDouble*1000000);
            // tempLong *= 1000000;
        }
        else if (tempStr.substring(tempStr.length()-1, tempStr.length()).toLowerCase().equals("k")) {
            tempStr = tempStr.substring(0, tempStr.length()-1);
            tempDouble = Double.parseDouble(tempStr);
            tempLong = (long)(tempDouble*1000);
            // tempLong *= 1000;
        }
        else
            tempLong = Long.parseLong(tempStr);
        
        setBuyPrice_long(tempLong);
        
//        if (temp <= 0 || temp > Integer.MAX_VALUE) {
//            this.buyPrice.set("1");
//            this.buyPrice_long = 1;
//        }
//        else {
//            this.buyPrice.set(String.valueOf(temp));
//            this.buyPrice_long = temp;
//        } // else
    } // setBuyPrice
    
    public void setBuyPrice_long(long buyPrice) {
        if (buyPrice <= 0 /*|| buyPrice > Integer.MAX_VALUE*/) {
            this.buyPrice.set("1");
            this.buyPrice_long = 1;
        }
        else {
            this.buyPrice.set(String.valueOf(buyPrice));
            this.buyPrice_long = buyPrice;
        } // else
        
        this.profit = calculateProfit();
    } // setBuyPrice_long
    
    public void setBought(boolean bought) {
        this.bought.set(bought);
    } // setBought
    
    
    // Price is stored as long in case in the future we can accept values greater than MAX_INT
    // Has data validation to make sure it currently falls in bounds of [0, MAX_INT]
    public void setSellPrice(String sellPrice) {
        String tempStr = removeCommas(sellPrice);
        double tempDouble;
        long tempLong;
        
        // Allow for 'b', 'm' and 'k' shortcuts in price
        if (tempStr.substring(tempStr.length()-1, tempStr.length()).toLowerCase().equals("b")) {
            tempStr = tempStr.substring(0, tempStr.length()-1);
            tempDouble = Double.parseDouble(tempStr);
            tempLong = (long)(tempDouble*1000000000);
        }
        else if (tempStr.substring(tempStr.length()-1, tempStr.length()).toLowerCase().equals("m")) {
            tempStr = tempStr.substring(0, tempStr.length()-1);
            tempDouble = Double.parseDouble(tempStr);
            tempLong = (long)(tempDouble*1000000);
        }
        else if (tempStr.substring(tempStr.length()-1, tempStr.length()).toLowerCase().equals("k")) {
            tempStr = tempStr.substring(0, tempStr.length()-1);
            tempDouble = Double.parseDouble(tempStr);
            tempLong = (long)(tempDouble*1000);
        }
        else
            tempLong = Long.parseLong(tempStr);
        
        setSellPrice_long(tempLong);
        
//        if (temp < 0 || temp > Integer.MAX_VALUE) {
//            this.sellPrice.set("0");
//            this.sellPrice_long = 0;
//        }
//        else {
//            this.sellPrice.set(String.valueOf(temp));
//            this.sellPrice_long = temp;
//        } // else
    } // setSellPrice
    
    public void setSellPrice_long(long sellPrice) {
        if (sellPrice < 0 /*|| sellPrice > Integer.MAX_VALUE*/) {
            this.sellPrice.set("0");
            this.sellPrice_long = 0;
        }
        else {
            this.sellPrice.set(String.valueOf(sellPrice));
            this.sellPrice_long = sellPrice;
        } // else
        
        this.profit = calculateProfit();
    } // setSellPrice_long
    
    public void setSellDate(LocalDateTime sellDate) {
        this.sellDate = sellDate;
    } // setSellDate
    
    public void setProfit(long profit) {
        this.profit = profit;
    } // setProfit
    
    public void setProfit_String(String profit) {
        setProfit(Long.parseLong(removeCommas(profit)));
    } // setProfit_String
    
    
    
    
    
    // HELPER METHODS
    // This function takes in an int, turns it into a string and appropriately inserts commas
    private String commaDelineate_int(int str) {
        StringBuilder result = new StringBuilder(String.valueOf(str));
        int limit = (str < 0) ? 1 : 0; // if str < 0, set limit to 1, else set limit to 0
        
        for (int i = result.length()-3; i > limit; i -= 3)
            result.insert(i, ",");
        
        return (result.toString());
    } // commaDelineate_int
    
    
    // This function takes in an long, turns it into a string and appropriately inserts commas
    private String commaDelineate_long(long str) {
        StringBuilder result = new StringBuilder(String.valueOf(str));
        int limit = (str < 0) ? 1 : 0; // if str < 0, set limit to 1, else set limit to 0
        
        for (int i = result.length()-3; i > limit; i -= 3)
            result.insert(i, ",");
        
        return (result.toString());
    } // commaDelineate_long
    
    
    // This function takes in a string and appropriately inserts commas
    private String commaDelineate_String(String str) {
        StringBuilder result = new StringBuilder(str);
        
        for (int i = result.length()-3; i > 0; i -= 3)
            result.insert(i, ",");
        
        return (result.toString());
    } // commaDelineate_String
    

    // This function takes in a string, removes commas, and returns the modified string
    private String removeCommas(String str) {
        return str.replace(",", "");
    } // removeCommas
    
    
    public boolean equals(Offer off) {
        return (itemName.equals(off.getItemName()) && account.equals(off.getAccount()));
        // quantity == off.getQuantity() &&
    } // equals
    
    private long calculateProfit() {
        return (sellPrice_long - buyPrice_long)*quantity;
    } // calculateProfit
    
    
    
    
    
    // TO STRING
    public String toString_active() {
        return (account + " " + quantity + " " + getItemName_saveFormat() + " " + getBuyDate()
                + " " + buyPrice.getValue() + " " + bought.getValue() + " " + sellPrice.getValue());
    } // toString [returns ActiveOffer]
    
    
    public String toString() {
        return (account + " " + quantity + " " + getItemName_saveFormat() + " " + getBuyDate()
                + " " + buyPrice.getValue() + " " + bought.getValue() + " " + sellPrice.getValue()
                + " " + getSellDate() + " " + profit);
    } // toString [returns CompleteOffer]
    
} // Offer
