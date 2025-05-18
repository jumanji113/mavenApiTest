import models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static assertions.Conditions.*;
import static assertions.Conditions.haseStatusCode;

public class NegativeUserTests extends BaseApiTest{

    private static final String USER_FAIL_CREATED_MESSAGE = "Login already exist";
    private static final String USER_FAIL_CREATED_STATUS = "fail";
    private static final String ERROR_UNAUTHORIZED = "Unauthorized";
    private static final Integer USER_CREATED_STATUS_CODE = 201;
    private static final Integer USER_FAIL_CREATED_STATUS_CODE = 400;
    private static final Integer USER_FAIL_AUTH = 401;
    private static final Integer GET_USER_POSITIVE = 200;

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
                .should(haseStatusCode(USER_CREATED_STATUS_CODE));

        userService.register(user)
                .should(haseMessage(USER_FAIL_CREATED_MESSAGE))
                .should(haseStatus(USER_FAIL_CREATED_STATUS))
                .should(haseStatusCode(USER_FAIL_CREATED_STATUS_CODE));
    }

    @Test
    @Tag("negativeUserTest")
    @DisplayName("Негативная авторизацию несуществующего пользователя")
    public void negativeAuthTest() {
        User user = getRandomUser();

        userService.auth(user)
                .should(haseStatusCode(USER_FAIL_AUTH))
                .should(haseMessageError(ERROR_UNAUTHORIZED));
    }


    @Test
    @Tag("negativeUserTest")
    @DisplayName("Негативный тест на удаление пользователя Админа")
    public void deleteAdminUser() {
        User user = getAdminUser();

        String token = userService.auth(user)
                .should(haseStatusCode(GET_USER_POSITIVE))
                .asJwt();

        userService.deleteUser(token)
                .should(haseMessage("Cant delete base users"))
                .should(haseStatus("fail"))
                .should(haseStatusCode(USER_FAIL_CREATED_STATUS_CODE));
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
                .should(haseStatusCode(USER_FAIL_CREATED_STATUS_CODE));
    }
}
