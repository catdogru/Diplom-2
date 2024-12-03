package ru.yandex.stellar.burgers.client.constants;

import lombok.Getter;

@Getter
public enum HttpHeaders {
    AUTHORIZATION("Authorization");

    private final String value;

    HttpHeaders(String value) {
        this.value = value;
    }

}