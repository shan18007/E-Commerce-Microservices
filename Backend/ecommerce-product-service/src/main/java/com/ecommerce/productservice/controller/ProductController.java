package com.ecommerce.productservice.controller;

import com.ecommerce.common.dto.ProductDto;
import com.ecommerce.common.exception.ApiResponse;
import com.ecommerce.productservice.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.addProduct(productDto));
    }

    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.updateProduct(productDto));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(
            @PathVariable
            @Positive(message = "productId should be positive")
            @NumberFormat(style = NumberFormat.Style.NUMBER)
            Long productId
    ) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProductDto>> getProductListOfUser(
            @PathVariable
            @Positive(message = "userId should be positive")
            @NumberFormat(style = NumberFormat.Style.NUMBER)
            Long userId
    ) {
        return ResponseEntity.ok(productService.getProductListByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProductsList(@RequestParam int pageNo,@RequestParam int pageSize) {
        return ResponseEntity.ok(productService.getProductsList(PageRequest.of(pageNo,pageSize)));
    }




}
