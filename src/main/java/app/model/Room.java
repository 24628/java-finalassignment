package app.model;

import java.util.*;

public class Room {

    private String title;
    private int maxSeats;
    private final List<Showing> showingList = new ArrayList();;

    public Room(String title, int maxSeats) {
        this.title = title;
        this.maxSeats = maxSeats;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSeats() {
        return maxSeats;
    }

    public void setSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }

    public List<Showing> getShowingList() {
        return showingList;
    }

    public void addShowingList(Showing showing) {
        this.showingList.add(showing);
    }
}
