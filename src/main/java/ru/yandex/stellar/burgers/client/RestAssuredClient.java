package ru.yandex.stellar.burgers.client;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static ru.yandex.stellar.burgers.Constants.STELLAR_BURGERS_BASE_URI;

public abstract class RestAssuredClient {

    RequestSpecification createRequestSpecification() {
        return given().baseUri(STELLAR_BURGERS_BASE_URI).log().body();
    }
}
