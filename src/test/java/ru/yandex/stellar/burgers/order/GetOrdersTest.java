package ru.yandex.stellar.burgers.order;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellar.burgers.client.IngredientClient;
import ru.yandex.stellar.burgers.client.OrderClient;
import ru.yandex.stellar.burgers.client.UserClient;
import ru.yandex.stellar.burgers.model.ingredient.Ingredient;
import ru.yandex.stellar.burgers.model.ingredient.Ingredients;
import ru.yandex.stellar.burgers.model.user.UserData;
import ru.yandex.stellar.burgers.service.check.IngredientCheck;
import ru.yandex.stellar.burgers.service.check.OrderCheck;
import ru.yandex.stellar.burgers.service.check.UserCheck;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.stellar.burgers.Constants.*;
import static ru.yandex.stellar.burgers.Constants.UserConstants.*;
import static ru.yandex.stellar.burgers.model.ingredient.Ingredients.collectRandomIngredientList;

public class GetOrdersTest {
    private final OrderClient orderClient = new OrderClient();
    private final OrderCheck orderCheck = new OrderCheck();

    private final UserClient userClient = new UserClient();
    private final UserCheck userCheck = new UserCheck();
    private String userAccessToken;

    @Before
    public void setUp() {
        createUser();

        int ordersCount = RandomUtils.nextInt(1, 3);
        createOrders(ordersCount);
    }

    @After
    public void tearDown() {
        deleteUser();
    }

    @Test
    @Description("Get user orders and check success status")
    public void getUserOrders() {
        orderClient
                .getOrders(userAccessToken)
                .assertThat()
                .statusCode(HTTP_OK);
    }

    @Test
    @Description("Get orders as unauthorized user and check error message")
    public void getOrdersWithoutAuthorization() {
        orderClient
                .getOrders(null)
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and().body(SUCCESS_JSON_KEY, equalTo(false))
                .and().body(MESSAGE_JSON_KEY, equalTo(UNAUTHORIZED_USER_MESSAGE));
    }

    private void deleteUser() {
        if (userAccessToken != null)
            userCheck.deletedSuccessfully(userClient.deleteUser(userAccessToken));
    }

    private void createOrders(int ordersCount) {
        IngredientClient ingredientClient = new IngredientClient();
        IngredientCheck ingredientCheck = new IngredientCheck();
        List<Ingredient> availableIngredients = ingredientCheck.receivedSuccessfully(ingredientClient.getIngredients());
        for (int i = 0; i < ordersCount - 1; i++) {
            Ingredients randomIngredients = new Ingredients(collectRandomIngredientList(availableIngredients));
            orderCheck.createdSuccessfully(orderClient.createOrder(randomIngredients, userAccessToken));
        }
    }

    private void createUser() {
        UserData userData = new UserData(generateRandomEmail(), DEFAULT_PASSWORD, DEFAULT_USER_NAME);
        userAccessToken = userCheck.createdSuccessfully(userClient.createUser(userData)).getAccessToken();
    }
}