package ru.yandex.stellar.burgers.service.check;

import io.restassured.response.ValidatableResponse;
import ru.yandex.stellar.burgers.model.ingredient.Ingredient;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.stellar.burgers.Constants.IngredientConstants.DATA_JSON_KEY;
import static ru.yandex.stellar.burgers.Constants.SUCCESS_JSON_KEY;

public class IngredientCheck {
    public List<Ingredient> receivedSuccessfully(ValidatableResponse getIngredientsResponse) {
        Ingredient[] ingredientsArray = getIngredientsResponse
                .assertThat()
                .statusCode(HTTP_OK)
                .and().body(SUCCESS_JSON_KEY, equalTo(true))
                .extract().response().body().jsonPath().getObject(DATA_JSON_KEY, Ingredient[].class);

        return asList(ingredientsArray);
    }
}
