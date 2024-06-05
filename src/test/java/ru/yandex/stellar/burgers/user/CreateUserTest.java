package ru.yandex.stellar.burgers.user;

import org.junit.After;
import org.junit.Test;
import ru.yandex.stellar.burgers.client.UserClient;
import ru.yandex.stellar.burgers.model.user.AuthorizedUserData;
import ru.yandex.stellar.burgers.model.user.UserData;
import ru.yandex.stellar.burgers.service.check.UserCheck;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ru.yandex.stellar.burgers.Constants.UserConstants.*;

public class CreateUserTest {
    private final UserClient userClient = new UserClient();
    private final UserCheck userCheck = new UserCheck();
    private String createdUserToken;

    @After
    public void tearDown() {
        if (createdUserToken != null) {
            userCheck.deletedSuccessfully(userClient.deleteUser(createdUserToken));
        }
    }

    @Test
    public void createdSuccessfully() {
        UserData userData = new UserData(generateRandomEmail(), DEFAULT_PASSWORD, DEFAULT_USER_NAME);
        AuthorizedUserData createdUser = userCheck.createdSuccessfully(userClient.createUser(userData));

        assertTrue("Response 'success' field should be true", createdUser.isSuccess());
        assertFalse("Response access token should not be empty", createdUser.getAccessToken().isEmpty());
        assertFalse("Response refresh token should not be empty", createdUser.getRefreshToken().isEmpty());

        createdUserToken = createdUser.getAccessToken();
    }

    @Test
    public void createExistingUser() {
        UserData userData = new UserData(generateRandomEmail(), DEFAULT_PASSWORD, DEFAULT_USER_NAME);

        // first creation
        userClient.createUser(userData);
        // second creation
        userClient
                .createUser(userData)
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and().body(SUCCESS_JSON_KEY, equalTo(false))
                .and().body(MESSAGE_JSON_KEY, equalTo(USER_ALREADY_EXISTS_MESSAGE));
    }

    @Test
    public void createWithoutEmail() {
        UserData userData = new UserData(null, DEFAULT_PASSWORD, DEFAULT_USER_NAME);
        userClient
                .createUser(userData)
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and().body(SUCCESS_JSON_KEY, equalTo(false))
                .and().body(MESSAGE_JSON_KEY, equalTo(EMPTY_REQUIRED_FIELD_MESSAGE));
    }

    @Test
    public void createWithoutPassword() {
        UserData userData = new UserData(generateRandomEmail(), null, DEFAULT_USER_NAME);
        userClient
                .createUser(userData)
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and().body(SUCCESS_JSON_KEY, equalTo(false))
                .and().body(MESSAGE_JSON_KEY, equalTo(EMPTY_REQUIRED_FIELD_MESSAGE));
    }

    @Test
    public void createWithoutName() {
        UserData userData = new UserData(generateRandomEmail(), DEFAULT_PASSWORD, null);
        userClient
                .createUser(userData)
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and().body(SUCCESS_JSON_KEY, equalTo(false))
                .and().body(MESSAGE_JSON_KEY, equalTo(EMPTY_REQUIRED_FIELD_MESSAGE));
    }
}