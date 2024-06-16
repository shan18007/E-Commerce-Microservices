package com.ecommerce.common.exception;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class FieldError {
    private String fieldName;
    private String value;
    private String message;
}
