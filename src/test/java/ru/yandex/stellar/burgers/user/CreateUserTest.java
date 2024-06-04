package ru.yandex.stellar.burgers.user;

import org.junit.After;
import org.junit.Test;
import ru.yandex.stellar.burgers.client.UserClient;
import ru.yandex.stellar.burgers.model.user.AuthorizedUserData;
import ru.yandex.stellar.burgers.model.user.UserData;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ru.yandex.stellar.burgers.Constants.UserConstants.*;

public class CreateUserTest {
    public final UserClient userClient = new UserClient();
    private String createdUserToken;

    @Test
    public void createdSuccessfully() {
        UserData userData = new UserData(generateRandomEmail(), DEFAULT_PASSWORD, DEFAULT_USER_NAME);
        AuthorizedUserData response = userClient
                .createUser(userData)
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(AuthorizedUserData.class);

        assertTrue("Response 'success' field should be true", response.isSuccess());
        assertFalse("Response access token should not be empty", response.getAccessToken().isEmpty());
        assertFalse("Response refresh token should not be empty", response.getRefreshToken().isEmpty());

        createdUserToken = response.getAccessToken();
    }

    @Test
    public void createExistingUser() {
        UserData userData = new UserData(generateRandomEmail(), DEFAULT_PASSWORD, DEFAULT_USER_NAME);
        userClient.createUser(userData);
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

    @After
    public void tearDown() {
        if (createdUserToken != null)
            userClient
                    .deleteUser(createdUserToken)
                    .assertThat()
                    .statusCode(HTTP_ACCEPTED)
                    .and().body(SUCCESS_JSON_KEY, equalTo(true))
                    .and().body(MESSAGE_JSON_KEY, equalTo(USER_REMOVED_MESSAGE));
    }

}