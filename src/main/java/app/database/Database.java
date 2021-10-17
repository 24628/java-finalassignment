package app.database;

import app.enums.UserType;
import app.helpers.SHA512;
import app.model.User;

import java.util.ArrayList;
import java.util.List;

public class Database {

    public List<User> getAllUsers(){
        return generateUsers();
    }

    private List<User> generateUsers(){
        List<User> userList = new ArrayList<>();

        userList.add(new User("John", "Doe", UserType.admin, "JohnDoe@example.com", "0678787878", SHA512.encryptThisString("password")));
        userList.add(new User("Jane", "Doe", UserType.normal, "JaneDoe@example.com", "0678787878", SHA512.encryptThisString("password")));

        return userList;
    }
}
