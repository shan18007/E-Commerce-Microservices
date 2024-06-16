package com.ecommerce.productservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String productName;
    private Integer quantity;
    private Long price;
    private String description;

    @ManyToOne
    private Category category;
    private boolean isListedForSale = false;
    private boolean isDeleted = false;
    private Long paymentId;

    private Long userId;
}
