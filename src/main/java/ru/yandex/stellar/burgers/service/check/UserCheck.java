package ru.yandex.stellar.burgers.service.check;

import io.restassured.response.ValidatableResponse;
import ru.yandex.stellar.burgers.model.user.AuthorizedUserData;

import static java.net.HttpURLConnection.HTTP_ACCEPTED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.stellar.burgers.Constants.MESSAGE_JSON_KEY;
import static ru.yandex.stellar.burgers.Constants.SUCCESS_JSON_KEY;
import static ru.yandex.stellar.burgers.Constants.UserConstants.USER_REMOVED_MESSAGE;

public class UserCheck {
    public AuthorizedUserData createdSuccessfully(ValidatableResponse createResponse) {
        return createResponse
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(AuthorizedUserData.class);
    }

    public void deletedSuccessfully(ValidatableResponse deleteResponse) {
        deleteResponse
                .assertThat()
                .statusCode(HTTP_ACCEPTED)
                .and().body(SUCCESS_JSON_KEY, equalTo(true))
                .and().body(MESSAGE_JSON_KEY, equalTo(USER_REMOVED_MESSAGE));
    }
}
