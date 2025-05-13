import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import models.JwtAuth;
import models.User;
import models.info.ErrorInfoAuth;
import models.info.Info;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Tag("api")
public class UserApiTests extends BaseApiTest {

    private static final String USER_CREATED_MESSAGE = "User created";
    private static final String USER_FAIL_CREATED_MESSAGE = "Login already exist";
    private static final String USER_CREATED_STATUS = "success";
    private static final String USER_FAIL_CREATED_STATUS = "fail";
    private static final String ERROR_UNAUTHORIZED = "Unauthorized";

    @Test
    @Tag("positiveUserTest")
    @DisplayName("Позитивная регистрация пользователя")
    public void positiveRegisterUser() {

        int randomNumber = Math.abs(random.nextInt());
        User user = User.builder().login("alex" + randomNumber).pass("human" + randomNumber).build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath().getObject("info", Info.class);

        assertEquals(USER_CREATED_MESSAGE, info.getMessage());
        assertEquals(USER_CREATED_STATUS, info.getStatus());
    }

    @Test
    @Tag("negativeUserTest")
    @DisplayName("Негативная регистрация пользователя")
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
    @Tag("positiveUserTest")
    @DisplayName("Позитивный авторизация нового пользователя")
    public void positiveAuth() {

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

        assertNotNull(token);
    }

    @Test
    @Tag("negativeUserTest")
    @DisplayName("Негативный тест на авторизацию несуществующего пользователя")
    public void negativeAuthTest() {

        int randomNumber = Math.abs(random.nextInt(10000));
        JwtAuth jwtAuth = new JwtAuth("pass" + randomNumber, "login" + randomNumber);

        ErrorInfoAuth errorInfoAuth = given().contentType(ContentType.JSON)
                .body(jwtAuth)
                .post("/api/login")
                .then()
                .statusCode(401)
                .extract()
                .as(ErrorInfoAuth.class);

        assertEquals(ERROR_UNAUTHORIZED, errorInfoAuth.getError());
    }

    @Test
    @Tag("positiveUserTest")
    @DisplayName("Позитивный тест на получение данных пользователя по токену")
    public void postitveGetUser() {

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

        User userInfo = given()
                .auth()
                .oauth2(token)
                .get("/api/user")
                .then()
                .extract()
                .as(User.class);

        assertEquals(user.getLogin(), userInfo.getLogin());
        assertEquals(user.getPass(), userInfo.getPass());
    }

    @Test
    @DisplayName("Проверка обновления пароля пользователя")
    public void positiveUpdatePassUser() {
        int randomNumber = Math.abs(random.nextInt(10000));
        User user = User.builder().login("alex" + randomNumber).pass("human" + randomNumber).build();
        String oldPass = user.getPass();

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

        HashMap<String, String> pass = new HashMap<>();
        pass.put("password", "juman" + random.nextInt(10000));

        Map<String, String> map = given()
                .contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .body(pass)
                .put("/api/user")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("info", new TypeRef<>() {
                });

        assertEquals("success", map.get("status"));
        assertEquals("User password successfully changed", map.get("message"));

        JwtAuth jwtAuthNew = new JwtAuth(pass.get("password"), user.getLogin());

        String tokenNew = given().contentType(ContentType.JSON)
                .body(jwtAuthNew)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");

        User userInfo = given()
                .auth()
                .oauth2(tokenNew)
                .get("/api/user")
                .then()
                .extract()
                .as(User.class);

        assertNotEquals(oldPass, userInfo.getPass());

    }

    @Test
    @DisplayName("Негативный тест на изменение пароля админа")
    public void negativeUpdateAdmin() {
        JwtAuth jwtAuth = new JwtAuth("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuth)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");

        JwtAuth jwtAuth1 = JwtAuth.builder().password("pass" + random.nextInt(100)).build();

        Map<String, String> errorInfo = given()
                .contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .body(jwtAuth1)
                .put("/api/user")
                .then()
                .extract()
                .jsonPath()
                .getObject("info", new TypeRef<>() {
                });

        assertEquals("fail", errorInfo.get("status"));
        assertEquals("Cant update base users", errorInfo.get("message"));
    }

    //@Test
    //@DisplayName("Негативный тест на удаление пользователя Админа")

}
