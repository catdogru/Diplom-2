package ru.yandex.stellar.burgers.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellar.burgers.client.UserClient;
import ru.yandex.stellar.burgers.model.user.AuthorizedUserData;
import ru.yandex.stellar.burgers.model.user.UserCredentials;
import ru.yandex.stellar.burgers.model.user.UserData;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ru.yandex.stellar.burgers.Constants.UserConstants.*;

public class LoginUserTest {
    private final UserClient userClient = new UserClient();
    private UserData userData;
    private AuthorizedUserData createdUser;

    @Before
    public void setUp() {
        userData = new UserData(generateRandomEmail(), DEFAULT_PASSWORD, DEFAULT_USER_NAME);
        createdUser = userClient.createUser(userData)
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body(SUCCESS_JSON_KEY, equalTo(true))
                .extract()
                .body().as(AuthorizedUserData.class);

        assertFalse("Access token should not be empty", createdUser.getAccessToken().isEmpty());
    }

    @Test
    public void loggedInSuccessfully() {
        UserCredentials userCredentials = new UserCredentials(userData.getEmail(), userData.getPassword());
        AuthorizedUserData response = userClient
                .loginUser(userCredentials)
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(AuthorizedUserData.class);

        assertTrue("Response 'success' field should be true", response.isSuccess());
        assertFalse("Response access token should not be empty", response.getAccessToken().isEmpty());
        assertFalse("Response refresh token should not be empty", response.getRefreshToken().isEmpty());
    }

    @Test
    public void loginWithIncorrectEmail() {
        UserCredentials userCredentials = new UserCredentials(userData.getEmail() + "_incorrect", userData.getPassword());
        userClient
                .loginUser(userCredentials)
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body(MESSAGE_JSON_KEY, equalTo(INCORRECT_CREDENTIALS_MESSAGE));
    }

    @Test
    public void loginWithIncorrectPassword() {
        UserCredentials userCredentials = new UserCredentials(userData.getEmail(), userData.getPassword() + "_incorrect");
        userClient
                .loginUser(userCredentials)
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body(MESSAGE_JSON_KEY, equalTo(INCORRECT_CREDENTIALS_MESSAGE));
    }

    @After
    public void tearDown() {
        if (createdUser.getAccessToken() != null)
            userClient
                    .deleteUser(createdUser.getAccessToken())
                    .assertThat()
                    .statusCode(HTTP_ACCEPTED)
                    .and().body(SUCCESS_JSON_KEY, equalTo(true))
                    .and().body(MESSAGE_JSON_KEY, equalTo(USER_REMOVED_MESSAGE));
    }
}