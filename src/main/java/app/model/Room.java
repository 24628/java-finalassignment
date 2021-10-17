package app.model;

import java.util.List;

public class Room {

    private String title;
    private int seats;
    private List<Ticket> ticketList;

    public Room(String title, int seats, List<Ticket> ticketList) {
        this.title = title;
        this.seats = seats;
        this.ticketList = ticketList;
    }
}
