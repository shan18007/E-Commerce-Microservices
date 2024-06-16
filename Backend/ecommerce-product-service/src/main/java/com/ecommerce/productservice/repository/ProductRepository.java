package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    public Product findByUserIdAndProductId(Long userId, Long ProductId);

    public List<Product>  findAllByUserId(Long userId);

    @Query("From Product where userId in(:userId) and isDeleted=false")
    public Page<Product> getAllNonDeletedProductsByUserId(List<Long> userId, Pageable pageable);

}
