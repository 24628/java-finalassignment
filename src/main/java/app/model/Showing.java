package app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Showing {

    private LocalDateTime startTime;
    private Movie movie;
    private int currentSeats;
    private final List<Ticket> ticketList = new ArrayList();;

    public Showing(LocalDateTime startTime, Movie movie, int currentSeats) {
        this.startTime = startTime;
        this.movie = movie;
        this.currentSeats = currentSeats;
    }

    public void addTicketToList(Ticket ticket){
        ticketList.add(ticket);
    }

    public int getCurrentSeats() {
        return currentSeats;
    }

    public void setCurrentSeats(int currentSeats) {
        this.currentSeats = currentSeats;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
