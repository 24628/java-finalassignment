package app.database;

import app.enums.UserType;
import app.helpers.SHA512;
import app.model.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private final List<User> userList;
    private final List<Room> roomList;
    private final List<Movie> movieList;

    public Database() {
        userList = generateUsers();
        roomList = generateRooms();
        movieList = generateMovieList();
    }

    public void addShowing(Showing showing, int roomIndex) {
        roomList.get(roomIndex).addShowingList(showing);
    }

    public List<User> getAllUsers() {
        return userList;
    }

    public List<Room> getAllRooms() {
        return roomList;
    }

    public void addMovieToList(Movie movie) {
        movieList.add(movie);
    }

    public void addTicketToShowing(int roomIndex, int showingIndex, int amountOfSeats, Ticket ticket) {
        roomList.get(roomIndex).getShowingList().get(showingIndex).setCurrentSeats(
                roomList.get(roomIndex).getShowingList().get(showingIndex).getCurrentSeats() - amountOfSeats
        );

        roomList.get(roomIndex).getShowingList().get(showingIndex).addTicketToList(ticket);
    }

    public List<Movie> getAllMovies() {
        return movieList;
    }

    private List<User> generateUsers() {
        List<User> userList = new ArrayList<>();

        userList.add(new User("John", "Doe", UserType.admin, "JohnDoe@example.com", "0678787878", SHA512.encryptThisString("password")));
        userList.add(new User("Jane", "Doe", UserType.normal, "JaneDoe@example.com", "0678787878", SHA512.encryptThisString("password")));

        return userList;
    }

    private List<Movie> generateMovieList() {
        List<Movie> movieList = new ArrayList<>();

        movieList.add(new Movie(125, "No Time To die", 12.00));
        movieList.add(new Movie(92, "The addams family 19", 9.00));

        return movieList;
    }

    private List<Room> generateRooms() {
        List<Room> roomList = new ArrayList<>();

        Room room1 = new Room("Room 1", 200);
        Room room2 = new Room("Room 2", 100);

        room1.addShowingList(new Showing(LocalDateTime.of(2020, Month.NOVEMBER, 10, 20, 0), this.generateMovieList().get(0), room1.getSeats()));
        room1.addShowingList(new Showing(LocalDateTime.of(2020, Month.NOVEMBER, 10, 22, 30), this.generateMovieList().get(1), room1.getSeats()));

        room2.addShowingList(new Showing(LocalDateTime.of(2020, Month.NOVEMBER, 10, 20, 0), this.generateMovieList().get(1), room2.getSeats()));
        room2.addShowingList(new Showing(LocalDateTime.of(2020, Month.NOVEMBER, 10, 22, 0), this.generateMovieList().get(0), room2.getSeats()));

        roomList.add(room1);
        roomList.add(room2);

        return roomList;
    }
}
