package com.vti.config.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice

public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private String getMessage(String key) {
        return messageSource.getMessage(
                key,
                null,
                "Default message",
                LocaleContextHolder.getLocale());
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception exception) {

        String message = getMessage("Exception.message");
        String detailMessage = exception.getLocalizedMessage();
        int code = 500;
        String moreInformation = "http://localhost:8080/api/v1/exception/500";

        ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        String message = getMessage("NoHandlerFoundException.message")
                + exception.getHttpMethod() + " " + exception.getRequestURL();
        String detailMessage = exception.getLocalizedMessage();
        int code = 404;
        String moreInformation = "http://localhost:8080/api/v1/exception/404";

        ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);
        return new ResponseEntity<>(response, status);
    }

    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        String message = getMessageFromHttpRequestMethodNotSupportedException(exception);
        String detailMessage = exception.getLocalizedMessage();
        int code = 405;
        String moreInformation = "http://localhost:8080/api/v1/exception/405";

        ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);
        return new ResponseEntity<>(response, status);
    }

    private String getMessageFromHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        String message = exception.getMethod() + " " + getMessage("HttpRequestMethodNotSupportedException.message");
        for (HttpMethod method : exception.getSupportedHttpMethods()) {
            message += method + " ";
        }
        return message;
    }

    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {

        String message = getMessageFromHttpMediaTypeNotSupportedException(exception);
        String detailMessage = exception.getLocalizedMessage();
        int code = 415;
        String moreInformation = "http://localhost:8080/api/v1/exception/415";

        ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);
        return new ResponseEntity<>(response, status);
    }

    private String getMessageFromHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        String message = exception.getContentType() + " " + getMessage("HttpMediaTypeNotSupportedException.message");
        for (MediaType method : exception.getSupportedMediaTypes()) {
            message += method + ", ";
        }
        return message.substring(0, message.length() - 2);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            errors.put(field, message);
        }

        for (ObjectError globalError : ex.getBindingResult().getGlobalErrors()) {
            String objectName = globalError.getObjectName();
            String message = globalError.getDefaultMessage();
            errors.put(objectName, message);
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    // bean validation error
    @SuppressWarnings("rawtypes")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {

        String message = getMessage("MethodArgumentNotValidException.message");
        String detailMessage = exception.getLocalizedMessage();
        // error
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation violation : exception.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
        int code = 400;
        String moreInformation = "http://localhost:8080/api/v1/exception/400";

        ErrorResponse response = new ErrorResponse(message, detailMessage, errors, code, moreInformation);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        String message = exception.getParameterName() + " " + getMessage("MissingServletRequestParameterException.message");
        String detailMessage = exception.getLocalizedMessage();
        int code = 400;
        String moreInformation = "http://localhost:8080/api/v1/exception/400";

        ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);
        return new ResponseEntity<>(response, status);
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {

        String message = exception.getName() + " " + getMessage("MethodArgumentTypeMismatchException.message")
                + exception.getRequiredType().getName();
        String detailMessage = exception.getLocalizedMessage();
        int code = 400;
        String moreInformation = "http://localhost:8080/api/v1/exception/400";

        ErrorResponse response = new ErrorResponse(message, detailMessage, null, code, moreInformation);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}

