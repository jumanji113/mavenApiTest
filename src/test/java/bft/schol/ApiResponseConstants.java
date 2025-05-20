package bft.schol;

public enum ApiResponseConstants {
    // Позитивные ответы
    USER_CREATED("User created", "success", 201),
    GET_USER_POSITIVE(null, null, 200),
    PASSWORD_UPDATED("User password successfully deleted", "success", 200),
    USER_DELETED("User successfully deleted", "success", 200),

    // Негативные ответы
    LOGIN_ALREADY_EXISTS("Login already exist", "fail", 400),
    UNAUTHORIZED("Unauthorized", "fail", 401),
    CANT_DELETE_BASE_USERS("Cant delete base users", "fail", 400),
    CANT_UPDATE_BASE_USERS("Cant update base users", "fail", 400);

    private final String message;
    private final String status;
    private final int statusCode;

    ApiResponseConstants(String message, String status, int statusCode) {
        this.message = message;
        this.status = status;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public int getStatusCode() {
        return statusCode;
    }
}