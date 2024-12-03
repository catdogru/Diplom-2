package ru.yandex.stellar.burgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static ru.yandex.stellar.burgers.Constants.IngredientConstants.GET_INGREDIENTS_PATH;

public class IngredientClient extends RestAssuredClient {
    @Step
    public ValidatableResponse getIngredients() {
        return createRequestSpecification()
                .when()
                .get(GET_INGREDIENTS_PATH)
                .then()
                .log().all();
    }
}
