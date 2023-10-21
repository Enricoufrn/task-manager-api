package com.example.taskmanagerapi.domain.enumerations;

public enum MessageCode {
    // GENERIC
    INTERNAL_SERVER_ERROR("internal.server.error"),
    BAD_REQUEST("bad.request"),
    PERMISSION_DENIED("permission.denied"),
    // USER
    USER_NOT_FOUND("user.not.found"),
    USER_DELETE_SELF("user.delete.self"),
    // TASK
    TASK_NOT_FOUND("task.not.found"),
    // AUTHENTICATION
    INVALID_AUTHORIZATION_HEADER("invalid.authorization.header"),
    AUTHENTICATION_NOT_FINALLY("authentication.not.finally"),
    AUTHORIZATION_HEADER_NOT_FOUND("authorization.header.not.found"),
    TOKEN_NOT_FOUND("token.not.found"),
    INVALID_TOKEN("invalid.token"),
    INVALID_LOGIN("invalid.login"),
    INVALID_PASSWORD("invalid.password"),
    INVALID_CREDENTIALS("invalid.credentials"),
    ;

    private String code;

    MessageCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
