package com.commic.v1.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_REQUEST(400, "Invalid request"),
    PARAMETER_NOT_VALID(400, "Parameter not valid"),
    USER_NOT_FOUND(204, "User not found"),
    CHAPTER_NOT_FOUND(204, "Chapter not found"),
    VALIDATION_ERROR(400, "Validation error"),
    PARAMETER_MISSING(400, "Parameter missing"),
    CREATE_SUCCESS(200, "Create success"),
    CREATE_FAILED(400, "Create failed"),
    UPDATE_SUCCESS(200, "Update success"),
    UPDATE_FAILED(400, "Update failed"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED.value(), "Unauthenticated"),
    UNAUTHORIZED(HttpStatus.FORBIDDEN.value(), "You do not have permission"),
    CATEGORY_EMPTY(204, "Category empty"),
    NOTIFICATION_EMPTY(204, "Notification empty"),
    FOUND(200, "Found"),
    NOT_FOUND(400, "Not found"),
    INVALID_PASSWORD(400, "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character");

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
}
