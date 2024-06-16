package com.ecommerce.productservice.helper;

import com.ecommerce.common.dto.CategoryDto;
import com.ecommerce.common.dto.ProductDto;
import com.ecommerce.productservice.entity.Category;
import com.ecommerce.productservice.entity.Product;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductHelper {

    public ProductDto toDto(Product product) {
        ProductDto productDto = ProductDto.builder().build();
        BeanUtils.copyProperties(product, productDto);
        productDto.setCategory(toCategoryDto(product.getCategory()));
        return productDto;
    }
    public List<ProductDto> toDtoList(List<Product> productList) {
        List<ProductDto> dtoList = new ArrayList<>();
        productList.forEach(product -> dtoList.add(toDto(product)));
        return dtoList;
    }


    public Product toEntity(ProductDto productDto) {
        Product product = Product.builder().build();
        BeanUtils.copyProperties(productDto, product);
        product.setCategory(toCategoryEntity(productDto.getCategory()));
        return product;
    }

    public List<Product> toEntityList(List<ProductDto> dtoList){
        List<Product> entityList =new ArrayList<>();
        dtoList.forEach(productDto -> entityList.add(toEntity(productDto)));
        return entityList;
    }

    public Category toCategoryEntity(CategoryDto categoryDto){
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        return category;
    }

    public CategoryDto toCategoryDto(Category category){
        CategoryDto categoryDto = new CategoryDto();
        BeanUtils.copyProperties(category, categoryDto);
        return categoryDto;
    }

    public List<CategoryDto> toCategoryDtoList(List<Category> categoryList) {
        List<CategoryDto> dtoList = new ArrayList<>();
        categoryList.forEach(category -> dtoList.add(toCategoryDto(category)));
        return dtoList;
    }
}
