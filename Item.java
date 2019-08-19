// Benjamin Maltbie - Final
// TransactionTracker Application Solution: Item.java

// Helper class for scraping data from Wikipedia page
public class Item {
	private String icon;
    private String typeIcon;
    private Day30 day30;
    private Today today;
    private String type;
    private String id;
    private String description;
    private String name;
    private String icon_large;
    private Current current;
    private Day90 day90;
    private Day180 day180;
    private String members;
    
    public String getIcon () {
        return icon;
    } // getIcon

    public void setIcon (String icon) {
        this.icon = icon;
    } // setIcon

    public String getTypeIcon () {
        return typeIcon;
    } // getTypeIcon

    public void setTypeIcon (String typeIcon) {
        this.typeIcon = typeIcon;
    } // setTypeIcon

    public Day30 getDay30 () {
        return day30;
    } // getDay30

    public void setDay30 (Day30 day30) {
        this.day30 = day30;
    } // setDay30

    public Today getToday () {
        return today;
    } // getToday

    public void setToday (Today today) {
        this.today = today;
    } // setToday

    public String getType () {
        return type;
    } // getType

    public void setType (String type) {
        this.type = type;
    } // setType

    public String getId () {
        return id;
    } // getId

    public void setId (String id) {
        this.id = id;
    } // setId

    public String getDescription () {
        return description;
    } // getDescription

    public void setDescription (String description) {
        this.description = description;
    } // setDescription

    public String getName () {
        return name;
    } // getName

    public void setName (String name) {
        this.name = name;
    } // setName

    public String getIcon_large () {
        return icon_large;
    } // getIcon_large

    public void setIcon_large (String icon_large) {
        this.icon_large = icon_large;
    } // setIcon_large

    public Current getCurrent () {
        return current;
    } // getCurrent

    public void setCurrent (Current current) {
        this.current = current;
    } // setCurrent

    public Day90 getDay90 () {
        return day90;
    } // getDay90

    public void setDay90 (Day90 day90) {
        this.day90 = day90;
    } // setDay90

    public Day180 getDay180 () {
        return day180;
    } // getDay180

    public void setDay180 (Day180 day180) {
        this.day180 = day180;
    } // setDay180

    public String getMembers () {
        return members;
    } // getMembers

    public void setMembers (String members) {
        this.members = members;
    } // setMembers

    @Override
    public String toString() {
        return "[icon = "+icon+", typeIcon = "+typeIcon+", day30 = "+day30+", today = "+today+", type = "+type+", id = "+id+", description = "+description+", name = "+name+", icon_large = "+icon_large+", current = "+current+", day90 = "+day90+", day180 = "+day180+", members = "+members+"]";
    } // toString
} // Item
