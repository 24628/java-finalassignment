package app.model;

public class TableHolderRooms {

    private String start;
    private String end;
    private String title;
    private String seats;
    private String price;

    public TableHolderRooms(String start, String end, String title, String seats, String price) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.seats = seats;
        this.price = price;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getTitle() {
        return title;
    }

    public String getSeats() {
        return seats;
    }

    public String getPrice() {
        return price;
    }
}
