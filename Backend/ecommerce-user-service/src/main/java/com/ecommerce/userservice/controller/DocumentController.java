package com.ecommerce.userservice.controller;

import com.ecommerce.common.dto.Document;
import com.ecommerce.userservice.service.DocumentService;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users/doc")
public class DocumentController {

    private DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Document> uploadDocument(
            @PathVariable("userId")
            @Positive(message = "userId must be a positive number")
            @NumberFormat(style = NumberFormat.Style.NUMBER)
            Long userId,
            @RequestPart("file") MultipartFile document,
            @RequestPart("type") String docType
    ) throws IOException {
        return ResponseEntity.ok(documentService.uploadDocument(document, userId, docType));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Document> updateDocument(
            @PathVariable("userId")
            @Positive(message = "userId must be a positive number")
            @NumberFormat(style = NumberFormat.Style.NUMBER)
            Long userId,
            @RequestPart("file") MultipartFile document,
            @RequestPart("type") String docType,
            @Positive(message = "docId must be a positive number")
            @NumberFormat(style = NumberFormat.Style.NUMBER)
            @RequestPart("docId") Long docId
    ) throws IOException {
        return ResponseEntity.ok(documentService.updateDocument(document, userId, docId));
    }

    @GetMapping("/{userId}/{docId}")
    public ResponseEntity<Document> getDocument(
            @Positive(message = "docId must be a positive number")
            @NumberFormat(style = NumberFormat.Style.NUMBER)
            @PathVariable("docId") Long docId
    ) throws IOException {
        return ResponseEntity.ok(documentService.getDocumentById(docId));
    }

    @DeleteMapping("/{userId}/{docId}")
    public ResponseEntity<String> deleteDocument(
            @PathVariable("userId")
            @Positive(message = "userId must be a positive number")
            @NumberFormat(style = NumberFormat.Style.NUMBER)
            Long userId,
            @Positive(message = "docId must be a positive number")
            @NumberFormat(style = NumberFormat.Style.NUMBER)
            @PathVariable("docId") Long docId
    ) throws IOException {
        documentService.deleteDocument(userId,docId);
        return ResponseEntity.ok("Document Successfully deleted.");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Document>> getAllDocumentOfUser(
            @PathVariable("userId")
            @Positive(message = "userId must be a positive number")
            @NumberFormat(style = NumberFormat.Style.NUMBER)
            Long userId
    ) throws IOException {
        return ResponseEntity.ok(documentService.getAllDocumentsOfUser(userId));
    }

}
