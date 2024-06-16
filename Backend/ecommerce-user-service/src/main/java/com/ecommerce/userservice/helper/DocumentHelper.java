package com.ecommerce.userservice.helper;

import com.ecommerce.userservice.entity.Document;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class DocumentHelper {

    public static Document toEntity(com.ecommerce.common.dto.Document document) {
        Document documentEntity = Document.builder().build();
        BeanUtils.copyProperties(document, documentEntity);

        return documentEntity;
    }


    public static com.ecommerce.common.dto.Document toDto(Document document) {
        com.ecommerce.common.dto.Document documentDto = com.ecommerce.common.dto.Document.builder().build();
        BeanUtils.copyProperties(document, documentDto);
        com.ecommerce.common.dto.User user = com.ecommerce.common.dto.User.builder().userId(document.getUser().getUserId()).build();
        documentDto.setUser(user);
        return documentDto;
    }

    public static List<com.ecommerce.common.dto.Document> toDtoList(List<Document> documentList) {
        List<com.ecommerce.common.dto.Document> documentDtoList = new ArrayList<>();
        documentList.forEach(document -> documentDtoList.add(toDto(document)));
        return documentDtoList;
    }

}
