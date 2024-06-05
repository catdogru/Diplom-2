package ru.yandex.stellar.burgers.order;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellar.burgers.client.IngredientClient;
import ru.yandex.stellar.burgers.client.OrderClient;
import ru.yandex.stellar.burgers.client.UserClient;
import ru.yandex.stellar.burgers.model.ingredient.Ingredient;
import ru.yandex.stellar.burgers.model.ingredient.Ingredients;
import ru.yandex.stellar.burgers.model.order.OrderData;
import ru.yandex.stellar.burgers.model.user.UserData;
import ru.yandex.stellar.burgers.service.check.IngredientCheck;
import ru.yandex.stellar.burgers.service.check.OrderCheck;
import ru.yandex.stellar.burgers.service.check.UserCheck;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ru.yandex.stellar.burgers.Constants.UserConstants.*;
import static ru.yandex.stellar.burgers.model.ingredient.Ingredients.collectRandomIngredientList;

public class CreateOrderTest {
    private final OrderClient orderClient = new OrderClient();
    private final OrderCheck orderCheck = new OrderCheck();

    private final IngredientClient ingredientClient = new IngredientClient();
    private final IngredientCheck ingredientCheck = new IngredientCheck();
    private final UserClient userClient = new UserClient();
    private final UserCheck userCheck = new UserCheck();
    private List<Ingredient> availableIngredients;
    private String userAccessToken;

    @Before
    public void setUp() {
        availableIngredients = ingredientCheck.receivedSuccessfully(ingredientClient.getIngredients());

        UserData userData = new UserData(generateRandomEmail(), DEFAULT_PASSWORD, DEFAULT_USER_NAME);
        userAccessToken = userCheck.createdSuccessfully(userClient.createUser(userData)).getAccessToken();
    }

    @After
    public void tearDown() {
        if (userAccessToken != null)
            userCheck.deletedSuccessfully(userClient.deleteUser(userAccessToken));
    }

    @Test
    public void createdSuccessfully() {
        Ingredients randomIngredients = new Ingredients(collectRandomIngredientList(availableIngredients));
        OrderData createdOrder = orderCheck.createdSuccessfully(orderClient.createOrder(randomIngredients, userAccessToken));
        assertTrue("Response 'success' field should be true", createdOrder.isSuccess());
        assertNotNull("Order number should not be null", createdOrder.getOrder().getNumber());
    }

    // Только авторизованные пользователи могут делать заказы
    @Test
    public void createAsUnauthorizedUser() {
        Ingredients randomIngredients = new Ingredients(collectRandomIngredientList(availableIngredients));
        orderClient
                .createOrder(randomIngredients, null)
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    public void createWithoutIngredients() {

    }

    @Test
    public void createWithUnknownIngredient() {

    }
}
/*
Создание заказа:

    с авторизацией,
    без авторизации,
    с ингредиентами,
    без ингредиентов,
    с неверным хешем ингредиентов.
 */