package bft.schol.negativeUserTests;

import bft.schol.ApiResponseConstants;
import bft.schol.BaseApiTest;
import io.qameta.allure.Epic;
import models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static assertions.Conditions.*;
import static assertions.Conditions.haseStatusCode;
import static bft.schol.ApiResponseConstants.*;

@Epic("Негативные Api тесты")
public class NegativeUserTests extends BaseApiTest {

    public User getRandomUser() {
        int randomNumber = Math.abs(BaseApiTest.random.nextInt());
        return User.builder()
                .login("alex" + randomNumber)
                .pass("human" + randomNumber)
                .build();
    }

    public User getAdminUser() {
        return User.builder()
                .pass("admin")
                .login("admin")
                .build();
    }

    @Test
    @Tag("negativeUserTest")
    @DisplayName("Негативная регистрация пользователя 400")
    public void negativeRegisterUser() {
        User user = getRandomUser();

        userService.register(user)
                .should(haseStatusCode(USER_CREATED.getStatusCode()));

        userService.register(user)
                .should(haseMessage(LOGIN_ALREADY_EXISTS.getMessage()))
                .should(haseStatus(LOGIN_ALREADY_EXISTS.getStatus()))
                .should(haseStatusCode(LOGIN_ALREADY_EXISTS.getStatusCode()));
    }

    @Test
    @Tag("negativeUserTest")
    @DisplayName("Негативная авторизацию несуществующего пользователя")
    public void negativeAuthTest() {
        User user = getRandomUser();

        userService.auth(user)
                .should(haseStatusCode(ApiResponseConstants.UNAUTHORIZED.getStatusCode()))
                .should(haseMessageError(ApiResponseConstants.UNAUTHORIZED.getMessage()));
    }


    @Test
    @Tag("negativeUserTest")
    @DisplayName("Негативный тест на удаление пользователя Админа")
    public void deleteAdminUser() {
        User user = getAdminUser();

        String token = userService.auth(user)
                .should(haseStatusCode(GET_USER_POSITIVE.getStatusCode()))
                .asJwt();

        userService.deleteUser(token)
                .should(haseMessage(CANT_DELETE_BASE_USERS.getMessage()))
                .should(haseStatus(CANT_DELETE_BASE_USERS.getStatus()))
                .should(haseStatusCode(CANT_DELETE_BASE_USERS.getStatusCode()));
    }

    @Test
    @Tag("negativeUserTest")
    @DisplayName("Негативный тест на изменение пароля админа")
    public void negativeUpdateAdmin() {
        User user = getAdminUser();
        String token = userService.auth(user).asJwt();

        String updatedPass = "newPass" + random.nextInt(100);

        userService.updatePass(updatedPass, token)
                .should(haseMessage("Cant update base users"))
                .should(haseStatus("fail"))
                .should(haseStatusCode(CANT_UPDATE_BASE_USERS.getStatusCode()));
    }
}
