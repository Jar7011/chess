package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    private HashMap<String, UserData> users = new HashMap<>();


    @Override
    public void createUser(UserData userData) {
        UserData newUser = new UserData(userData.username(), userData.password(), userData.email());
        users.put(newUser.username(), newUser);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void clear() {
        users.clear();
    }
}
