import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import models.JwtAuth;
import models.User;
import models.info.Info;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static assertions.Conditions.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class UserApiTestRef extends BaseApiTest {

    private static final String USER_CREATED_MESSAGE = "User created";
    private static final String USER_FAIL_CREATED_MESSAGE = "Login already exist";
    private static final String USER_CREATED_STATUS = "success";
    private static final String USER_FAIL_CREATED_STATUS = "fail";
    private static final String ERROR_UNAUTHORIZED = "Unauthorized";
    private static final Integer USER_CREATED_STATUS_CODE = 201;
    private static final Integer GET_USER_POSITIVE = 200;
    private static final Integer USER_FAIL_CREATED_STATUS_CODE = 400;
    private static final Integer USER_FAIL_AUTH = 401;

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
    @Tag("positiveUserTest")
    @DisplayName("Регистрация пользователя 201")
    public void positiveRegisterUser() {
        User user = getRandomUser();

        userService.register(user)
                .should(haseStatusCode(USER_CREATED_STATUS_CODE))
                .should(haseMessage(USER_CREATED_MESSAGE))
                .should(haseStatus(USER_CREATED_STATUS));
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
    @Tag("positiveUserTest")
    @DisplayName("Авторизация админа")
    public void positiveAuthAdmin() {
        User user = getAdminUser();
        String token = userService.auth(user).asJwt();

        assertNotNull(token);
    }

    @Test
    @Tag("positiveUserTest")
    @DisplayName("Авторизация нового пользователя")
    public void positiveAuthNewUser() {
        User user = getRandomUser();

        userService.register(user);
        String token = userService.auth(user).asJwt();

        assertNotNull(token);
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
    @Tag("positiveUserTest")
    @DisplayName("Получение данных пользователя по токену")
    public void postitveGetUser() {
        User user = getRandomUser();
        userService.register(user)
                .should(haseStatusCode(USER_CREATED_STATUS_CODE));

        String token = userService.auth(user)
                .asJwt();

        userService.getUserInfo(token)
                .should(haseStatusCode(GET_USER_POSITIVE));
    }

    @Test
    @DisplayName("Проверка обновления пароля пользователя")
    public void positiveUpdatePassUser() {
        User user = getRandomUser();
        String oldPass = user.getPass();

        userService.register(user);

        String token = userService.auth(user)
                .asJwt();

        String newPass = "human" + random.nextInt(1000);
        userService.updatePass(newPass, token)
                .should(haseStatusCode(GET_USER_POSITIVE))
                .should(haseMessage("User password successfully changed"));

        user.setPass(newPass);
        String tokenNew = userService.auth(user)
                .asJwt();

        User userInfo = userService.getUserInfo(tokenNew)
                .as(User.class);

        assertNotEquals(oldPass, userInfo.getPass());
    }

    @Test
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
    @Tag("positiveUserTest")
    @DisplayName("Позитивный тест на удаление пользователя")
    public void postiveDeleteUser() {
        User user = getRandomUser();

        userService.register(user)
                .should(haseStatusCode(USER_CREATED_STATUS_CODE));

        String token = userService.auth(user)
                .should(haseStatusCode(GET_USER_POSITIVE))
                .asJwt();

        userService.deleteUser(token)
                .should(haseStatusCode(GET_USER_POSITIVE))
                .should(haseMessage("User successfully deleted"));
    }
}
