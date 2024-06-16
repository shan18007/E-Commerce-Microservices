package com.ecommerce.productservice.service;

import com.ecommerce.common.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    public CategoryDto addCategory(CategoryDto category);
    public CategoryDto updateCategory(CategoryDto category);

    public void deleteCategory(Long[] categoryId);

    public List<CategoryDto> getAllCategory();

    public CategoryDto getCategory(Long categoryId);


}
