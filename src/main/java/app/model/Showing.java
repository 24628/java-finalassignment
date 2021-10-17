package app.model;

import java.time.LocalDateTime;

public class Showing {

    private LocalDateTime startTime;
    private Movie movie;
    private int currentSeats;

    public Showing(LocalDateTime startTime, Movie movie, int currentSeats) {
        this.startTime = startTime;
        this.movie = movie;
        this.currentSeats = currentSeats;
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
