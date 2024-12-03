package ru.yandex.stellar.burgers.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class AuthorizedUserData {
    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;
    private String message;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        String email;
        String name;
    }
}