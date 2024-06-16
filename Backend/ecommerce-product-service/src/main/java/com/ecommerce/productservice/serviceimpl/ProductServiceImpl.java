package com.ecommerce.productservice.serviceimpl;

import com.ecommerce.common.dto.CategoryDto;
import com.ecommerce.common.dto.ProductDto;
import com.ecommerce.common.dto.User;
import com.ecommerce.common.exception.ApiResponse;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.productservice.entity.Category;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.exception.util.ExceptionUtil;
import com.ecommerce.productservice.helper.ProductHelper;
import com.ecommerce.productservice.repository.CategoryRepository;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.service.CategoryService;
import com.ecommerce.productservice.service.ProductService;
import com.ecommerce.productservice.service.UserServiceClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl implements ProductService, CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
    ProductRepository productRepository;

    CategoryRepository categoryRepository;

    ProductHelper productHelper;

    UserServiceClient userService;

    ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductHelper productHelper, UserServiceClient userService){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productHelper = productHelper;
        this.userService = userService;
    }
    @Override
    public ProductDto addProduct(ProductDto productDto) {
        try{
            ResponseEntity<User> user = userService.getUser(productDto.getUserId());
        } catch (Exception e ) {
            if(e instanceof FeignException.FeignClientException feignClientException) {
                ExceptionUtil.handleException(feignClientException);
            } else {
                throw new BusinessException(e.getMessage());
            }
        }
        getCategoryByCategoryId(productDto.getCategory().getCategoryId());
        Product product = productHelper.toEntity(productDto);
        LOG.info("saving product - {}",productDto);
        Product savedProduct = this.productRepository.save(product);
        return productHelper.toDto(savedProduct);
    }

    @Override
    public ProductDto getProduct(Long productId) {
        Product product = getProductByProductId(productId).get();
        return productHelper.toDto(product);
    }

    @Override
    public Optional<Product> getProductByProductId(Long productId) {
        return Optional.ofNullable(this.productRepository.findById(productId).orElseThrow(() -> new BusinessException("Invalid productId")));
    }


    @Override
    public Page<ProductDto> getProductsList(Pageable pageable) {

        Page<Product> allProducts = productRepository.findAll(pageable);
        List<Product> productList = allProducts.getContent();

        LOG.info("getProductList ");
        List<Long> activeUserIdList = new ArrayList<>();
        try{
            ResponseEntity<Page<User>> allActiveNonDeletedUserResponse = userService.getAllActiveNonDeletedUser(0, 10);
            activeUserIdList = allActiveNonDeletedUserResponse.getBody().stream().map(User::getUserId).distinct().collect(Collectors.toList());

            List<Product> activeUserProductList = allProducts.getContent();

            for(Product p: productList) {
                if(!activeUserIdList.contains(p.getUserId())) {
                    p.setQuantity(0);
                    p.setDescription("Not available");
                } else{

                }
            }
        } catch (Exception e ) {
            if(e instanceof FeignException.FeignClientException feignClientException) {
                ExceptionUtil.handleException(feignClientException);
            } else {
                throw new BusinessException(e.getMessage());
            }
        }
        return getProductListByUserIdList(userIdsList, pageable);
    }

    @Override
    public Product getProductByUserId(Long userId, Long ProductId) {
        //user should be active and not deleted.
        return this.productRepository.findByUserIdAndProductId(userId,ProductId);
    }

    @Override
    public List<ProductDto> getProductListByUserId(Long userId) {
        //user should be active and not deleted.
        List<Product> userProductsList = this.productRepository.findAllByUserId(userId);
        return productHelper.toDtoList(userProductsList);
    }

    @Override
    public Page<ProductDto> getProductListByUserIdList(List<Long> userIdList,Pageable pageable) {
        Page<Product> userProductsList = this.productRepository.getAllNonDeletedProductsByUserId(userIdList,pageable);
        List<Product> produtctList = userProductsList.getContent();
        List<ProductDto> productDtoList = productHelper.toDtoList(produtctList);
        return new PageImpl<>(productDtoList);
    }


    private void validateUser(User user) {
        if(!user.isActive() || user.isDeleted()) throw new BusinessException("User is deactivated or deleted. !");
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        try{
            ResponseEntity<User> user = userService.getUser(productDto.getUserId());
        } catch (Exception e ) {
            if(e instanceof FeignException.FeignClientException feignClientException) {
                ExceptionUtil.handleException(feignClientException);
            } else {
                throw new BusinessException(e.getMessage());
            }
        }
        Product existingProduct = getProductByProductId(productDto.getProductId()).get();
        existingProduct.setProductName(productDto.getProductName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setQuantity(productDto.getQuantity());
        Optional<Category> existingCategory = getCategoryByCategoryId(productDto.getCategory().getCategoryId());
        existingProduct.setCategory(existingCategory.get());
        LOG.info("updating product - {} ", productDto);
        Product updatedProduct = this.productRepository.save(productHelper.toEntity(productDto));
        return productHelper.toDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Long[] productId) {
        //To DO -> Delete operation should be allowed only for admin and product manager
        // LoggedIn user should be not deleted and active.
        List<Product> productsList = this.productRepository.findAllById(List.of(productId));
        List<Long> invalidProductIds = Stream.of(productId).filter(id -> !productsList.contains(id)).collect(Collectors.toList());
        if(!invalidProductIds.isEmpty()) {
            LOG.info("Invalid products id's {}",invalidProductIds);
            throw new BusinessException("Invalid product id's - "+invalidProductIds);
        }
        for (Product p: productsList) {
                p.setListedForSale(false);
        }
        LOG.info("deleting products with id's {}",productId);
        this.productRepository.saveAll(productsList);
    }

    @Override
    public void deleteProductsByUserId(Long[] userId) {
        //To DO -> Delete operation should be allowed only for admin and product manager
        // LoggedIn user should be not deleted and active.
    }

    @Override
    public CategoryDto addCategory(CategoryDto category) {
        validateCategory(category.getCategoryName(),0L);
        Category categoryEntity = productHelper.toCategoryEntity(category);
        LOG.info("Adding category {}",category);
        return productHelper.toCategoryDto(categoryRepository.save(categoryEntity));
    }

    private void validateCategory(String categoryName, Long categoryId) {
        if(categoryName != null) {
            getCategoryByCategoryName(categoryName).ifPresent(category -> { throw new BusinessException("Category already exists.!"); });
        }
        if(categoryId != null && categoryId > 0) {
            getCategoryByCategoryId(categoryId).ifPresent(category -> { throw new BusinessException("Category already exists.!"); });;
        }
    }
    @Override
    public CategoryDto updateCategory(CategoryDto category) {
        getCategoryByCategoryId(category.getCategoryId());
        Category upfatedCategory = categoryRepository.save(productHelper.toCategoryEntity(category));
        return productHelper.toCategoryDto(upfatedCategory);
    }

    @Override
    public void deleteCategory(Long[] categoryId) {
        List<Category> categoryList = categoryRepository.findAllById(List.of(categoryId));
        List<Long> invalidCategoryIds = Stream.of(categoryId).filter(id -> !categoryList.contains(id)).collect(Collectors.toList());
        if(!invalidCategoryIds.isEmpty()) {
            LOG.info("Invalid category Id's - {}", invalidCategoryIds);
            throw new BusinessException("Invalid category id's - "+invalidCategoryIds);
        }
        LOG.info("deleting category with id's - {}",categoryId);
        for(Category category: categoryList) {
            category.setDeleted(true);
        }
        categoryRepository.saveAll(categoryList);
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        return productHelper.toCategoryDtoList(categoryRepository.findAll());
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        return productHelper.toCategoryDto(getCategoryByCategoryId(categoryId).get());
    }

    private Optional<Category> getCategoryByCategoryName(String categoryName) {
      return categoryRepository.findByCategoryName(categoryName);
    }

    private Optional<Category> getCategoryByCategoryId(Long categoryId) {
        if(categoryId == null || (categoryId != null && categoryId <= 0L)) {
            throw new BusinessException("Invalid categoryId");
        }
        return Optional.ofNullable(categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Invalid CategoryId.!")));
    }
}
