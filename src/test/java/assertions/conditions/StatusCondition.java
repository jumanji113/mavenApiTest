package assertions.conditions;

import assertions.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import models.info.Info;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public class StatusCondition implements Condition {

    private final String expectedStatus;

    @Override
    public void check(ValidatableResponse response) {
        Info infoResult = response.extract().jsonPath().getObject("info", Info.class);
        assertEquals(expectedStatus, infoResult.getStatus());
    }
}
