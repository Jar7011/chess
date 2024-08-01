package UnitTests;


import dataaccess.DataAccessException;
import dataaccess.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UnitTests {

    UserData newUser = new UserData("username", "password", "email");
    UserData expectedUser = new UserData("username", "password", "email");
    SQLUserDAO user = new SQLUserDAO();

    @BeforeEach
    void cleanStart() throws DataAccessException {
        user.clear();
    }

    @Test
    void passCreateUser() throws DataAccessException {
        user.createUser(newUser);
        assertEquals(user.getUser("username").username(), expectedUser.username());
    }

    @Test
    void failCreateUser() throws DataAccessException {
        user.createUser(newUser);
        assertThrows(DataAccessException.class, () -> user.createUser(newUser));
    }
}
