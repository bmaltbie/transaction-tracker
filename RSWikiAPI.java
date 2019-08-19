// Benjamin Maltbie - Final
// TransactionTracker Application Solution: RSWikiAPI.java

// Helper class for scraping data from Wikipedia page
public class RSWikiAPI {
	private Item item;

    public Item getItem() {
        return item;
    } // getItem

    public void setItem (Item item) {
        this.item = item;
    } // setItem

    @Override
    public String toString() {
        return "[item = "+item+"]";
    } // toString
} // RSWikiAPI
