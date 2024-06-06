package ru.yandex.stellar.burgers.user;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellar.burgers.client.UserClient;
import ru.yandex.stellar.burgers.model.user.AuthorizedUserData;
import ru.yandex.stellar.burgers.model.user.UserCredentials;
import ru.yandex.stellar.burgers.model.user.UserData;
import ru.yandex.stellar.burgers.service.check.UserCheck;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ru.yandex.stellar.burgers.Constants.MESSAGE_JSON_KEY;
import static ru.yandex.stellar.burgers.Constants.UserConstants.*;

public class LoginUserTest {
    private final UserClient userClient = new UserClient();
    private final UserCheck userCheck = new UserCheck();
    private UserData userData;
    private String userAccessToken;

    @Before
    public void setUp() {
        userAccessToken = createUser().getAccessToken();
        assertFalse("Access token should not be empty", userAccessToken.isEmpty());
    }

    @After
    public void tearDown() {
        deleteUser();
    }

    @Test
    @Description("Create and log in user, check logged in user authorization data is not empty")
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
    @Description("Try to login with invalid email and check error message")
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
    @Description("Try to login with invalid password and check error message")
    public void loginWithIncorrectPassword() {
        UserCredentials userCredentials = new UserCredentials(userData.getEmail(), userData.getPassword() + "_incorrect");
        userClient
                .loginUser(userCredentials)
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body(MESSAGE_JSON_KEY, equalTo(INCORRECT_CREDENTIALS_MESSAGE));
    }

    private AuthorizedUserData createUser() {
        userData = new UserData(generateRandomEmail(), DEFAULT_PASSWORD, DEFAULT_USER_NAME);
        return userCheck.createdSuccessfully(userClient.createUser(userData));
    }

    private void deleteUser() {
        if (userAccessToken != null)
            userCheck.deletedSuccessfully(userClient.deleteUser(userAccessToken));
    }
}