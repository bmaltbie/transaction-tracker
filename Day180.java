// Benjamin Maltbie - Final
// TransactionTracker Application Solution: Day180.java

// Helper class for scraping data from Wikipedia page
public class Day180 {
	private String trend;
    private String change;

    public String getTrend () {
        return trend;
    } // getTrend

    public void setTrend (String trend) {
        this.trend = trend;
    } // setTrend

    public String getChange () {
        return change;
    } // getChange

    public void setChange (String change) {
        this.change = change;
    } // setChange

    @Override
    public String toString() {
        return "[trend = "+trend+", change = "+change+"]";
    } // toString
} // Day180
