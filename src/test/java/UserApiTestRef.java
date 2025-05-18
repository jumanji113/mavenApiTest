import models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.List;
import static assertions.Conditions.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserApiTestRef extends BaseApiTest {

    private static final String USER_CREATED_MESSAGE = "User created";
    private static final String USER_CREATED_STATUS = "success";
    private static final Integer USER_CREATED_STATUS_CODE = 201;
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
    @Tag("positiveUserTest")
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
    @Tag("positiveUserTest")
    @DisplayName("Позитивный тест на удаление пользователя")
    public void positiveDeleteUser() {
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

    @Test
    @Tag("positiveUserTest")
    @DisplayName("Позитивный тест на получение всех пользователей")
    public void getAllUsers() {
        List<String> users = userService.getAllUsers().asList(String.class);
        assertFalse(users.isEmpty());
    }
}
