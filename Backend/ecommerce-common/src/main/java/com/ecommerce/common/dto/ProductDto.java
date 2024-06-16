package com.ecommerce.common.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductDto {

    private Long productId;

    @NotBlank(message = "Product name should not be empty.")
    @Size(min = 4, max = 200, message = "product name should be min 4 and max 200")
    private String productName;
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Minimum quantity should be one")
    private Integer quantity;
    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Minimum price should be one")
    private Long price;
    @NotBlank(message = "Description is required")
    @Size(min = 20, max = 999999, message = "Description must be between 20 and 999999")
    private String description;

    @NotNull(message = "category is required")
    @Valid
    private CategoryDto category;
    private boolean isListedForSale = false;
    @NotNull(message = "payment id is required")
    private Long paymentId;
    @NotNull(message = "userId is required")
    @Min(value = 1, message = "userId must be greater than 1")
    private Long userId;
}
