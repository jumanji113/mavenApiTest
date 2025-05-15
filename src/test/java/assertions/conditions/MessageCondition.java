package assertions.conditions;

import assertions.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import models.info.Info;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public class MessageCondition implements Condition {

    private final String expectedMessage;

    @Override
    public void check(ValidatableResponse response) {

        Info info = response.extract().jsonPath().getObject("info", Info.class);
        assertEquals(expectedMessage, info.getMessage());
    }
}
