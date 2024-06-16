package com.ecommerce.userservice.repo;

import com.ecommerce.userservice.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("FROM Document d where d.user.userId=:userId AND d.deleted = false")
    public List<Document> findDocumentByUserId(@Param("userId") Long userId);

}
