package app.database;

import app.enums.UserType;
import app.helpers.SHA512;
import app.model.Movie;
import app.model.Room;
import app.model.Showing;
import app.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public List<User> getAllUsers(){
        return generateUsers();
    }

    public List<Room> getAllRooms(){
        return generateRooms();
    }

    private List<User> generateUsers(){
        List<User> userList = new ArrayList<>();

        userList.add(new User("John", "Doe", UserType.admin, "JohnDoe@example.com", "0678787878", SHA512.encryptThisString("password")));
        userList.add(new User("Jane", "Doe", UserType.normal, "JaneDoe@example.com", "0678787878", SHA512.encryptThisString("password")));

        return userList;
    }

    private List<Room> generateRooms(){
        List<Room> roomList = new ArrayList<>();

        Room room1 = new Room("Room 1", 200);
        Room room2 = new Room("Room 2", 100);

        Movie movieNoTimeToDie = new Movie(125, "No Time To die", 12.00);
        Movie movieFamily = new Movie(92, "The addams family 19", 9.00);

        room1.addShowingList(new Showing(LocalDateTime.of(2020, Month.NOVEMBER, 10, 20, 0), movieNoTimeToDie, room1.getSeats()));
        room1.addShowingList(new Showing(LocalDateTime.of(2020, Month.NOVEMBER, 10, 22, 30), movieFamily, room1.getSeats()));

        room2.addShowingList(new Showing(LocalDateTime.of(2020, Month.NOVEMBER, 10, 20, 0), movieFamily, room2.getSeats()));
        room2.addShowingList(new Showing(LocalDateTime.of(2020, Month.NOVEMBER, 10, 22, 0), movieNoTimeToDie, room2.getSeats()));

        roomList.add(room1);
        roomList.add(room2);

        return roomList;
    }
}
