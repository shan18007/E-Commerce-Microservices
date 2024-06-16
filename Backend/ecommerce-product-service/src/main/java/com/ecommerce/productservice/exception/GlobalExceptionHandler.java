package com.ecommerce.productservice.exception;

import com.ecommerce.common.exception.ApiResponse;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import org.apache.commons.lang.StringUtils;
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

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> businessExceptionHandler(BusinessException ex){
        ApiResponse response = ApiResponse.builder()
                .status("FAILED")
                .success(false)
                .message(StringUtils.isEmpty(ex.getMessage()) ? "BAD REQUEST" : ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .fieldErrors(ex.getFieldErrors())
                .mapList(ex.getMapList())
                .build();
        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> ResourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        ApiResponse response = ApiResponse.builder()
                .status("FAILED")
                .success(false)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(StringUtils.isEmpty(ex.getMessage()) ? "RESOURCE NOT FOUND.!" : ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    };


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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> exception(Exception ex) {
        ApiResponse response = ApiResponse.builder()
                .status("FAILED")
                .success(false)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(StringUtils.isEmpty(ex.getMessage()) ? "" : ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }
}
