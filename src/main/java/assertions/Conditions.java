package assertions;

import assertions.conditions.ErrorCondition;
import assertions.conditions.MessageCondition;
import assertions.conditions.StatusCodeCondition;
import assertions.conditions.StatusCondition;

public class Conditions {
    public static MessageCondition haseMessage(String expectedMessage) {
        return new MessageCondition(expectedMessage);
    }

    public static StatusCondition haseStatus(String expectedStatus){
        return new StatusCondition(expectedStatus);
    }

    public static StatusCodeCondition haseStatusCode(Integer expectedStatusCode){
        return new StatusCodeCondition(expectedStatusCode);
    }

    public static ErrorCondition haseMessageError(String expectedErrorMessage){
        return new ErrorCondition(expectedErrorMessage);
    }
}
