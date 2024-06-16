package com.ecommerce.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Filter {

    @NotBlank(message = "columnName is required")
    @Size(min = 2, max = 128, message = "columnName value should be min 2 and max 128 characters")
    private String columnName;
    private Condition condition;
    @NotBlank(message = "value is required")
    @Size(min = 2, max = 128, message = "value should be min 2 and max 128 characters")
    private Object value;

}
