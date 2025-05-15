package services;

import assertions.AssertableResponse;
import io.restassured.http.ContentType;
import models.User;
import models.JwtAuth;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserService {

    public AssertableResponse register(User user){
        return new AssertableResponse(given().contentType(ContentType.JSON)
                .body(user)
                .post("/signup")
                .then());
    }

    public AssertableResponse getUserInfo(String jwt){
        return new AssertableResponse(given().auth().oauth2(jwt)
                .get("/user")
                .then());
    }

    public AssertableResponse getUserInfo(){
        return new AssertableResponse(given()
                .get("/user")
                .then());
    }

    public AssertableResponse updatePass(String newPassword, String jwt){
        Map<String, String> password = new HashMap<>();
        password.put("password", newPassword);

        return new AssertableResponse(given().contentType(ContentType.JSON)
                .auth().oauth2(jwt)
                .body(password)
                .put("/user")
                .then());
    }

    public AssertableResponse deleteUser(String jwt){
        return new AssertableResponse(given().auth().oauth2(jwt)
                .delete("/user")
                .then());
    }

    public AssertableResponse auth(User fullUser){
        JwtAuth data = new JwtAuth(fullUser.getPass(), fullUser.getLogin());
        return new AssertableResponse(given().contentType(ContentType.JSON)
                .body(data)
                .post("/login")
                .then());
    }

    public AssertableResponse getAllUsers(){
        return new AssertableResponse(given()
                .get("/users")
                .then());
    }
}
