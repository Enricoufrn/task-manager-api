package com.example.taskmanagerapi.exceptions;

import com.example.taskmanagerapi.domain.enumerations.MessageCode;
import com.example.taskmanagerapi.dtos.ApiError;
import com.example.taskmanagerapi.helper.MessageHelper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
    private MessageHelper messageHelper;

    public RestExceptionHandler(MessageHelper messageHelper) {
        this.messageHelper = messageHelper;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception exception) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), this.messageHelper.getMessage(MessageCode.INTERNAL_SERVER_ERROR), "");
        return getResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomAuthorizationException.class)
    public ResponseEntity<ApiError> handlerCustomAuthenticationExceptions(CustomAuthorizationException customAuthorizationException) {
        ApiError apiError = new ApiError(customAuthorizationException.getHttpStatus().getReasonPhrase(), customAuthorizationException.getMessage(), customAuthorizationException.getDetails());
        return getResponseEntity(apiError, customAuthorizationException.getHttpStatus());
    }

    @ExceptionHandler(GenericCustomException.class)
    public ResponseEntity<ApiError> handlerGenerateTokenExceptions(GenericCustomException genericCustomException) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), genericCustomException.getMessage(), genericCustomException.getDetails());
        return getResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException dataIntegrityViolationException) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.getReasonPhrase(), this.messageHelper.getMessage(MessageCode.BAD_REQUEST), dataIntegrityViolationException.getMostSpecificCause().getMessage());
        return getResponseEntity(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        StringBuilder details = new StringBuilder();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> details.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append(" "));
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.getReasonPhrase(), this.messageHelper.getMessage(MessageCode.BAD_REQUEST), details.toString());
        return getResponseEntity(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ApiError> handleHttpMessageErrorConversionException(HttpMessageConversionException exception){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.getReasonPhrase(), this.messageHelper.getMessage(MessageCode.BAD_REQUEST), exception.getMessage());
        return getResponseEntity(apiError, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiError> getResponseEntity(ApiError apiError, HttpStatus httpStatus) {
        return new ResponseEntity<>(apiError, httpStatus);
    }
}
