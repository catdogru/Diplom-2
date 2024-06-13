package ru.yandex.stellar.burgers;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class Constants {
    public static final String STELLAR_BURGERS_BASE_URI = "https://stellarburgers.nomoreparties.site";
    public static final String API_PATH = "/api";

    // common json keys
    public static final String SUCCESS_JSON_KEY = "success";
    public static final String MESSAGE_JSON_KEY = "message";

    // common messages
    public static final String UNAUTHORIZED_USER_MESSAGE = "You should be authorised";

    public static class UserConstants {
        // api path
        public static final String CREATE_USER_PATH = API_PATH + "/auth/register";
        public static final String LOGIN_USER_PATH = API_PATH + "/auth/login";
        public static final String UPDATE_USER_PATH = API_PATH + "/auth/user";
        public static final String DELETE_USER_PATH = API_PATH + "/auth/user";

        // response message
        public static final String USER_ALREADY_EXISTS_MESSAGE = "User already exists";
        public static final String EMPTY_REQUIRED_FIELD_MESSAGE = "Email, password and name are required fields";
        public static final String USER_REMOVED_MESSAGE = "User successfully removed";
        public static final String INCORRECT_CREDENTIALS_MESSAGE = "email or password are incorrect";
        public static final String EMAIL_ALREADY_IN_USE_MESSAGE = "User with such email already exists";

        //data
        public static final String DEFAULT_PASSWORD = "Qwerty12345";
        public static final String DEFAULT_USER_NAME = "Тестовый Тест Тестович";

        public static String generateRandomEmail() {
            return "testEmail_" + randomNumeric(4) + "@yandex.ru";
        }
    }

    public static class OrderConstants {
        // api path
        public static final String CREATE_ORDER_PATH = API_PATH + "/orders";
        public static final String GET_ORDERS_PATH = API_PATH + "/orders";

        // response message
        public static final String INGREDIENT_MUST_BE_PROVIDED_MESSAGE = "Ingredient ids must be provided";

        // data
        public static final String INVALID_INGREDIENT = "INVALID_INGREDIENT";
    }

    public static class IngredientConstants {
        // api path
        public static final String GET_INGREDIENTS_PATH = API_PATH + "/ingredients";

        // json keys
        public static final String DATA_JSON_KEY = "data";
    }
}
