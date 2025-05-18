package assertions.conditions;

import assertions.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import models.info.ErrorInfoAuth;
import static org.junit.jupiter.api.Assertions.assertEquals;


@RequiredArgsConstructor
public class ErrorCondition implements Condition {

    private final String expectedMessage;

    @Override
    public void check(ValidatableResponse response) {
        ErrorInfoAuth errorInfoAuth = response.extract().as(ErrorInfoAuth.class);
        assertEquals(expectedMessage, errorInfoAuth.getError());
    }
}
