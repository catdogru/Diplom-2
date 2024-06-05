package ru.yandex.stellar.burgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.yandex.stellar.burgers.model.ingredient.Ingredients;

import static io.restassured.http.ContentType.JSON;
import static ru.yandex.stellar.burgers.Constants.OrderConstants.CREATE_ORDER_PATH;
import static ru.yandex.stellar.burgers.Constants.OrderConstants.GET_ORDERS_PATH;
import static ru.yandex.stellar.burgers.client.constants.HttpHeaders.AUTHORIZATION;

public class OrderClient extends RestAssuredClient {
    @Step
    public ValidatableResponse createOrder(Ingredients ingredients, String token) {
        RequestSpecification specification = createRequestSpecification();
        if (token != null && !token.isEmpty()) specification.header(AUTHORIZATION.getValue(), token);
        return specification
                .contentType(JSON)
                .body(ingredients)
                .when()
                .post(CREATE_ORDER_PATH)
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse getOrders() {
        return createRequestSpecification()
                .when()
                .get(GET_ORDERS_PATH)
                .then()
                .log().all();
    }
}
