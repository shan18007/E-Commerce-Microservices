package com.ecommerce.userservice.exception;

import com.ecommerce.common.exception.ApiResponse;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        List<com.ecommerce.common.exception.FieldError> fieldErrorList = fieldErrors.stream()
                .map(fieldError -> com.ecommerce.common.exception.FieldError.builder()
                        .fieldName(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        var response = ApiResponse.builder()
                .status("FAILED")
                .message("Validation failed")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .fieldErrors(fieldErrorList)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> businessException(BusinessException exception) {
        ApiResponse response = ApiResponse.builder()
                .status("FAILED")
                .message(StringUtils.isEmpty(exception.getMessage()) ? "Bad Request" : exception.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .fieldErrors(exception.getFieldErrors())
                .mapList(exception.getMapList())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundException(ResourceNotFoundException exception) {
        ApiResponse response = ApiResponse.builder()
                .status("FAILED")
                .message(StringUtils.isEmpty(exception.getMessage()) ? "Resource not found !!!" : exception.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .fieldErrors(null)
                .mapList(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> exception(Exception exception) {

        ApiResponse response = ApiResponse.builder()
                .status("FAILED")
                .message(exception.getMessage() == null ? "Something went wrong." : exception.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .fieldErrors(null)
                .mapList(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

}
