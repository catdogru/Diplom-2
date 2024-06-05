package ru.yandex.stellar.burgers.service.check;

import io.restassured.response.ValidatableResponse;
import ru.yandex.stellar.burgers.model.order.OrderData;

import static java.net.HttpURLConnection.HTTP_OK;

public class OrderCheck {
    public OrderData createdSuccessfully(ValidatableResponse createResponse) {
        return createResponse
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(OrderData.class);
    }
}
