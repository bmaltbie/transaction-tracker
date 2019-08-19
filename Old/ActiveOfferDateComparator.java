// Benjamin Maltbie – Midterm 2
// TransactionTracker Application Solution: ActiveOfferDateComparator.java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class ActiveOfferDateComparator implements Comparator<ActiveOffer> {
    @Override
    public int compare(ActiveOffer ao1, ActiveOffer ao2) {
        LocalDateTime d1 = ao1.getBuyDate_date();
        LocalDateTime d2 = ao2.getBuyDate_date();
        
        return d2.compareTo(d1); // descending order (new -> old), reverse to have ascending
    } // compare
    
} // ActiveOfferDateComparator
