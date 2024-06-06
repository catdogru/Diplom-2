package ru.yandex.stellar.burgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.yandex.stellar.burgers.model.ingredient.Ingredients;

import static io.restassured.http.ContentType.JSON;
import static ru.yandex.stellar.burgers.Constants.OrderConstants.CREATE_ORDER_PATH;
import static ru.yandex.stellar.burgers.client.constants.HttpHeaders.AUTHORIZATION;

public class OrderClient extends RestAssuredClient {
    @Step
    public ValidatableResponse createOrder(Ingredients ingredients, String token) {
        RequestSpecification specification = createRequestSpecification();
        if (token != null && !token.isEmpty()) specification.header(AUTHORIZATION.getValue(), token);
        if (ingredients != null) specification.body(ingredients);
        return specification
                .contentType(JSON)
                .when()
                .post(CREATE_ORDER_PATH)
                .then()
                .log().all();
    }
}
