package com.ecommerce.userservice.external.services;

import com.ecommerce.common.dto.Document;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

@FeignClient(name = "USER-SERVICE", path = "/users/doc", url = "http://localhost:8084")
public interface DocumentServiceClient {

    @GetMapping("/{userId}")
    public ResponseEntity<List<Document>> getAllDocumentOfUser(
            @PathVariable("userId")
            @Positive(message = "userId must be a positive number")
            @NumberFormat(style = NumberFormat.Style.NUMBER)
            Long userId
    );
}
