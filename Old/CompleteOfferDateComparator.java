// Benjamin Maltbie – Midterm 2
// TransactionTracker Application Solution: CompleteOfferDateComparator.java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class CompleteOfferDateComparator implements Comparator<CompleteOffer> {
    @Override
    public int compare(CompleteOffer co1, CompleteOffer co2) {
        LocalDateTime d1 = co1.getBuyDate_date();
        LocalDateTime d2 = co2.getBuyDate_date();
        
        return d2.compareTo(d1); // descending order (new -> old), reverse to have ascending
    } // compare
    
} // CompleteOfferDateComparator
