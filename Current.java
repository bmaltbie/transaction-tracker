// Benjamin Maltbie - Final
// TransactionTracker Application Solution: Current.java

// Helper class for scraping data from Wikipedia page
public class Current {
	private String price;
    private String trend;

    public String getPrice () {
        return price;
    } // getPrice

    public void setPrice (String price) {
        this.price = price;
    } // setPrice

    public String getTrend () {
        return trend;
    } // getTrend

    public void setTrend (String trend) {
        this.trend = trend;
    } // setTrend

    @Override
    public String toString() {
        return "[price = "+price+", trend = "+trend+"]";
    } // toString
} // Current
