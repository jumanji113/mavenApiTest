
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import listeners.CustomTpl;
import org.junit.jupiter.api.BeforeAll;
import services.UserService;

import java.util.Random;

public class BaseApiTest {

    public static Random random;
    public static UserService userService;

    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "http://85.192.34.140:8080/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
        random = new Random();
        userService = new UserService();
    }
}
