import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import models.JwtAuth;
import models.User;
import models.info.ErrorInfoAuth;
import models.info.Info;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiTest extends BaseApiTest {

    private static final String USER_CREATED_MESSAGE = "User created";
    private static final String USER_FAIL_CREATED_MESSAGE = "Login already exist";
    private static final String USER_CREATED_STATUS = "success";
    private static final String USER_FAIL_CREATED_STATUS = "fail";
    private static final String ERROR_UNAUTHORIZED = "Unauthorized";

    @Test
    @DisplayName("Позитивная регистрация пользователя")
    public void positiveRegisterUser() {
        int randomNumber = Math.abs(random.nextInt());
        User user = User.builder().login("alex" + randomNumber).pass("human" + randomNumber).build();
        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .extract()
                .jsonPath().getObject("info", Info.class);
        assertEquals(USER_CREATED_MESSAGE, info.getMessage());
        assertEquals(USER_CREATED_STATUS, info.getStatus());
    }

    @Test
    @DisplayName("Позитивная регистрация пользователя")
    public void negativeRegisterUser() {
        int randomNumber = Math.abs(random.nextInt());
        User user = User.builder().login("alex" + randomNumber).pass("human" + randomNumber).build();
        given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201);
        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(400)
                .extract()
                .jsonPath()
                .getObject("info", Info.class);
        assertEquals(USER_FAIL_CREATED_MESSAGE, info.getMessage());
        assertEquals(USER_FAIL_CREATED_STATUS, info.getStatus());
    }

    @Test
    @DisplayName("Позитивный авторизация нового пользователя")
    public void positiveAuth(){
        int randomNumber = Math.abs(random.nextInt(10000));
        User user = User.builder().login("alex" + randomNumber).pass("human" + randomNumber).build();
        given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201);

        JwtAuth jwtAuth = new JwtAuth(user.getPass(), user.getLogin());

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuth)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
        Assertions.assertNotNull(token);
    }

    @Test
    @DisplayName("Негативный тест на авторизацию несуществующего пользователя")
    public void negativeAuthTest(){
        int randomNumber = Math.abs(random.nextInt(10000));
        JwtAuth jwtAuth = new JwtAuth("pass" + randomNumber, "login" + randomNumber);
        ErrorInfoAuth errorInfoAuth = given().contentType(ContentType.JSON)
                .body(jwtAuth)
                .post("/api/login")
                .then()
                .statusCode(401)
                .extract()
                .as(ErrorInfoAuth.class);
        Assertions.assertEquals(ERROR_UNAUTHORIZED, errorInfoAuth.getError());
    }

}
