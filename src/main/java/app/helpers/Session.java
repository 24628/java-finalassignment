package app.helpers;

import app.model.ServiceDeskEmployee;
import app.model.User;

public final class Session {

    private static Session instance;
    private static User user;

    private Session(User user) {
        Session.user = user;
    }

    public static Session getInstance(User user) {
        if(instance == null) {
            instance = new Session(user);
        }
        return instance;
    }

    public static User getUser() {
        return user;
    }

    public static void destroy() {
        user = null;
    }

    public static boolean isServiceDeskEmployee(){
        return user instanceof ServiceDeskEmployee;
    }
}
