package com.ecommerce.userservice.service;

import com.ecommerce.common.dto.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {

    public Document uploadDocument(MultipartFile document, Long userId, String docType) throws IOException;

    public List<Document> getAllDocumentsOfUser(Long userId);

    public Document updateDocument(MultipartFile document, Long userId, Long docId) throws IOException;

    void deleteDocument(Long userId, Long docId) throws IOException;

    Document getDocumentById(Long docId);

}
