package com.ecommerce.common.exception;


import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ApiResponse {
    private String status;
    private int statusCode;
    private String message;
    private boolean success;

    List<FieldError> fieldErrors;
    private Map<String, Object> mapList;
}
