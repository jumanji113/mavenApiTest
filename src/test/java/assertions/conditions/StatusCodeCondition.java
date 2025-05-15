package assertions.conditions;

import assertions.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public class StatusCodeCondition implements Condition {

    private final Integer exptectedStatusCode;

    @Override
    public void check(ValidatableResponse response) {
        int actualStatus = response.extract().statusCode();
        assertEquals(exptectedStatusCode, actualStatus);
    }
}
