// Benjamin Maltbie - Final
// TransactionTracker Application Solution: OfferDateComparator.java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;


// Helper class so that I can sort lists of Offer objects
public class OfferDateComparator implements Comparator<Offer> {
    
    @Override
    public int compare(Offer o1, Offer o2) {
        LocalDateTime d1 = o1.getBuyDate_date();
        LocalDateTime d2 = o2.getBuyDate_date();
        
        return d2.compareTo(d1); // descending order (new -> old), reverse to have ascending
    } // compare
} // OfferDateComparator
