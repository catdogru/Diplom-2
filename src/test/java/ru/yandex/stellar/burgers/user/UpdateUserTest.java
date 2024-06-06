package ru.yandex.stellar.burgers.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellar.burgers.client.UserClient;
import ru.yandex.stellar.burgers.model.user.AuthorizedUserData;
import ru.yandex.stellar.burgers.model.user.AuthorizedUserData.User;
import ru.yandex.stellar.burgers.model.user.UserData;
import ru.yandex.stellar.burgers.service.check.UserCheck;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static ru.yandex.stellar.burgers.Constants.*;
import static ru.yandex.stellar.burgers.Constants.UserConstants.*;

public class UpdateUserTest {
    private final UserClient userClient = new UserClient();
    private final UserCheck userCheck = new UserCheck();
    private UserData beforeUpdateUserData;
    private AuthorizedUserData userForUpdate;
    private AuthorizedUserData anotherExistingUser;

    @Before
    public void setUp() {
        beforeUpdateUserData = new UserData(generateRandomEmail(), DEFAULT_PASSWORD, DEFAULT_USER_NAME);
        userForUpdate = userCheck.createdSuccessfully(userClient.createUser(beforeUpdateUserData));

        UserData anotherUserData = new UserData(generateRandomEmail(), DEFAULT_PASSWORD, DEFAULT_USER_NAME);
        anotherExistingUser = userCheck.createdSuccessfully(userClient.createUser(anotherUserData));

        assertFalse("Access token should not be empty", userForUpdate.getAccessToken().isEmpty());
        assertFalse("Access token should not be empty", anotherExistingUser.getAccessToken().isEmpty());
    }

    @After
    public void tearDown() {
        if (userForUpdate.getAccessToken() != null)
            userCheck.deletedSuccessfully(userClient.deleteUser(userForUpdate.getAccessToken()));

        if (anotherExistingUser.getAccessToken() != null)
            userCheck.deletedSuccessfully(userClient.deleteUser(anotherExistingUser.getAccessToken()));
    }

    @Test
    public void updateAll() {
        UserData newUserData = new UserData(
                beforeUpdateUserData.getEmail() + "_1",
                beforeUpdateUserData.getPassword() + "_1",
                beforeUpdateUserData.getName() + "_1"
        );

        User updatedUser = userClient
                .updateUser(newUserData, userForUpdate.getAccessToken())
                .assertThat()
                .statusCode(HTTP_OK)
                .body(SUCCESS_JSON_KEY, equalTo(true))
                .extract()
                .body().as(AuthorizedUserData.class)
                .getUser();

        assertEquals(newUserData.getEmail().toLowerCase(), updatedUser.getEmail().toLowerCase());
        assertEquals(newUserData.getName(), updatedUser.getName());
    }

    @Test
    public void updateEmail() {
        UserData newUserData = new UserData(
                userForUpdate.getUser().getEmail() + "_1",
                null,
                null
        );

        User updatedUser = userClient
                .updateUser(newUserData, userForUpdate.getAccessToken())
                .assertThat()
                .statusCode(HTTP_OK)
                .body(SUCCESS_JSON_KEY, equalTo(true))
                .extract()
                .body().as(AuthorizedUserData.class)
                .getUser();

        assertEquals(newUserData.getEmail().toLowerCase(), updatedUser.getEmail().toLowerCase());
    }

    @Test
    public void updateEmailWithExistingValue() {
        UserData newUserData = new UserData(
                anotherExistingUser.getUser().getEmail(),
                null,
                null
        );

        userClient
                .updateUser(newUserData, userForUpdate.getAccessToken())
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and().body(SUCCESS_JSON_KEY, equalTo(false))
                .and().body(MESSAGE_JSON_KEY, equalTo(EMAIL_ALREADY_IN_USE_MESSAGE));
    }

    @Test
    public void updateName() {
        UserData newUserData = new UserData(
                null,
                null,
                userForUpdate.getUser().getName() + "_1"
        );

        User updatedUser = userClient
                .updateUser(newUserData, userForUpdate.getAccessToken())
                .assertThat()
                .statusCode(HTTP_OK)
                .body(SUCCESS_JSON_KEY, equalTo(true))
                .extract()
                .body().as(AuthorizedUserData.class)
                .getUser();

        assertEquals(newUserData.getName(), updatedUser.getName());
    }

    @Test
    public void updateAllWithoutAuthorization() {
        UserData newUserData = new UserData(generateRandomEmail(), DEFAULT_PASSWORD, DEFAULT_USER_NAME + "1");
        userClient
                .updateUser(newUserData, null)
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and().body(SUCCESS_JSON_KEY, equalTo(false))
                .and().body(MESSAGE_JSON_KEY, equalTo(UNAUTHORIZED_USER_MESSAGE));
    }

    @Test
    public void updateEmailWithoutAuthorization() {
        UserData newUserData = new UserData(generateRandomEmail(), null, null);
        userClient
                .updateUser(newUserData, null)
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and().body(SUCCESS_JSON_KEY, equalTo(false))
                .and().body(MESSAGE_JSON_KEY, equalTo(UNAUTHORIZED_USER_MESSAGE));
    }

    @Test
    public void updateNameWithoutAuthorization() {
        UserData newUserData = new UserData(null, null, DEFAULT_USER_NAME + "1");
        userClient
                .updateUser(newUserData, null)
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and().body(SUCCESS_JSON_KEY, equalTo(false))
                .and().body(MESSAGE_JSON_KEY, equalTo(UNAUTHORIZED_USER_MESSAGE));
    }
}