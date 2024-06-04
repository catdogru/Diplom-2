package ru.yandex.stellar.burgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.yandex.stellar.burgers.model.user.UserCredentials;
import ru.yandex.stellar.burgers.model.user.UserData;

import static io.restassured.http.ContentType.JSON;
import static ru.yandex.stellar.burgers.Constants.UserConstants.*;
import static ru.yandex.stellar.burgers.client.constants.HttpHeaders.AUTHORIZATION;

public class UserClient extends RestAssuredClient {
    @Step
    public ValidatableResponse createUser(UserData userData) {
        return createRequestSpecification()
                .contentType(JSON)
                .body(userData)
                .when()
                .post(CREATE_USER_PATH)
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse loginUser(UserCredentials credentials) {
        return createRequestSpecification()
                .contentType(JSON)
                .body(credentials)
                .when()
                .post(LOGIN_USER_PATH)
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse updateUser(UserData userData, String token) {
        RequestSpecification specification = createRequestSpecification();
        if (token != null && !token.isEmpty()) specification.header(AUTHORIZATION.getValue(), token);
        return specification
                .contentType(JSON)
                .body(userData)
                .when()
                .patch(UPDATE_USER_PATH)
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse deleteUser(String token) {
        return createRequestSpecification()
                .header(AUTHORIZATION.getValue(), token)
                .when()
                .delete(DELETE_USER_PATH)
                .then()
                .log().all();
    }
}
