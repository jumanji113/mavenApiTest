import io.restassured.http.ContentType;
import models.JwtAuth;
import models.User;
import models.info.Info;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static assertions.Conditions.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserApiTestRef extends BaseApiTest{

    private static final String USER_CREATED_MESSAGE = "User created";
    private static final String USER_FAIL_CREATED_MESSAGE = "Login already exist";
    private static final String USER_CREATED_STATUS = "success";
    private static final String USER_FAIL_CREATED_STATUS = "fail";
    private static final String ERROR_UNAUTHORIZED = "Unauthorized";
    private static final Integer USER_CREATED_STATUS_CODE = 201;
    private static final Integer USER_FAIL_CREATED_STATUS_CODE = 400;


    public User getRandomUser(){
        int randomNumber = Math.abs(random.nextInt());
        return User.builder()
                .login("alex" + randomNumber)
                .pass("human" + randomNumber)
                .build();
    }

    public User getAdminUser(){
        return User.builder()
                .pass("admin")
                .login("admin")
                .build();
    }

    @Test
    @Tag("positiveUserTest")
    @DisplayName("Позитивная регистрация пользователя")
    public void positiveRegisterUser() {
        User user = getRandomUser();

        userService.register(user)
                .should(haseStatusCode(USER_CREATED_STATUS_CODE))
                .should(haseMessage(USER_CREATED_MESSAGE))
                .should(haseStatus(USER_CREATED_STATUS));
    }

    @Test
    @Tag("negativeUserTest")
    @DisplayName("Негативная регистрация пользователя")
    public void negativeRegisterUser() {
        User user = getRandomUser();

        userService.register(user)
                        .should(haseStatusCode(201));

        userService.register(user)
                .should(haseMessage(USER_FAIL_CREATED_MESSAGE))
                .should(haseStatus(USER_FAIL_CREATED_STATUS))
                .should(haseStatusCode(USER_FAIL_CREATED_STATUS_CODE));
    }

    @Test
    @Tag("positiveUserTest")
    @DisplayName("Позитивный авторизация нового пользователя")
    public void positiveAuth() {

        User user = getRandomUser();

        userService.register(user)
                .should(haseStatusCode(201));

        //String token = userService.auth(user);

        //assertNotNull(token);
    }


}
