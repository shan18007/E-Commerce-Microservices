package com.ecommerce.common.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Document {

    private Long docId;

    private String name;

    private String path;

    private String type;

    private boolean verified;

    private boolean deleted;

    private User user;

}
