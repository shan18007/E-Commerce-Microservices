package com.ecommerce.userservice.serviceimpl;

import com.ecommerce.common.constant.Directory;
import com.ecommerce.common.constant.DocType;
import com.ecommerce.common.dto.Document;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.util.FileUtil;
import com.ecommerce.userservice.helper.DocumentHelper;
import com.ecommerce.userservice.repo.DocumentRepository;
import com.ecommerce.userservice.service.DocumentService;
import com.ecommerce.userservice.service.UserService;
import com.ecommerce.userservice.util.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    private DocumentRepository documentRepository;

    private UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Document uploadDocument(MultipartFile documentReceived, Long userId, String docType) throws IOException {

        if (documentReceived.isEmpty()) {
            throw new BusinessException("File con not be empty");
        }

        com.ecommerce.userservice.entity.Document document = com.ecommerce.userservice.entity.Document.builder()
                .name(documentReceived.getOriginalFilename())
                .type(DocType.AADHAR_CARD)
                .verified(false)
                .deleted(false)
                .build();

        com.ecommerce.userservice.entity.User user = userService.getUserByUserId(userId).orElseThrow(() -> new BusinessException("Invalid UserId"));

        user.getDocuments().stream()
                .filter(doc -> doc.getType().equals(docType)).findAny().ifPresent(existingDoc -> {
                    throw new BusinessException(docType+" type of documents already uploaded by you. We are not allowed to upload it again.");
                });

        String filePath = AppProperties.getInstance().getBasePath() + File.separator + Directory.USER_SERVICE +
                File.separator + user.getUsername() + File.separator + docType;

        FileUtil.saveFileInFolder(filePath, document.getName(), documentReceived.getBytes());

        document.setUser(user);
        document.setPath(filePath);

        com.ecommerce.userservice.entity.Document savedEntity = documentRepository.save(document);

        Document savedDto = Document.builder().build();
        BeanUtils.copyProperties(savedEntity, savedDto);

        return savedDto;
    }

    @Override
    public List<Document> getAllDocumentsOfUser(Long userId) {
        List<com.ecommerce.userservice.entity.Document> documentList = documentRepository.findDocumentByUserId(userId);
        return DocumentHelper.toDtoList(documentList);
    }

    @Override
    public Document updateDocument(MultipartFile documentFile, Long userId, Long docId) throws IOException {
        com.ecommerce.userservice.entity.Document existingDocument = getDocument(docId);
        existingDocument.setName(documentFile.getOriginalFilename());
        String existingDocumentPath = existingDocument.getPath();

        FileUtil.deleteFile(existingDocumentPath);
        LOG.info("Existing file removed and added new file - {}", documentFile.getOriginalFilename());

        String newFilePath = AppProperties.getInstance().getBasePath() + File.separator + Directory.USER_SERVICE +
                File.separator + existingDocument.getUser().getUsername() + File.separator + existingDocument.getType();

        FileUtil.saveFileInFolder(newFilePath, documentFile.getOriginalFilename(), documentFile.getBytes());
        existingDocument.setPath(newFilePath);

        com.ecommerce.userservice.entity.Document savedDocument = documentRepository.save(existingDocument);

        LOG.info("{} File updated.", documentFile.getOriginalFilename());
        return DocumentHelper.toDto(savedDocument);
    }

    @Override
    public void deleteDocument(Long userId, Long docId) throws IOException {
        com.ecommerce.userservice.entity.Document existingDocument = getDocument(docId);
        existingDocument.setDeleted(true);

        if(existingDocument.getUser().getUserId() != userId) {
            LOG.info("Document - {} is not associated with the provided userId - {}", docId, userId);
            throw new BusinessException("Document - "+docId+" is not associated with the provided userId - "+userId);
        }

        LOG.info("File - {} deleted", existingDocument.getName());
        com.ecommerce.userservice.entity.Document savedDocument = documentRepository.save(existingDocument);
    }

    public com.ecommerce.userservice.entity.Document getDocument(Long docId) {
        return documentRepository.findById(docId).orElseThrow(() -> new BusinessException("Invalid document Id"));
    }

    @Override
    public Document getDocumentById(Long docId) {
        return DocumentHelper.toDto(getDocument(docId));
    }

}
