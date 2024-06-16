package com.ecommerce.userservice.entity;

import com.ecommerce.common.constant.DocType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docId;

    private String name;

    private String path;

    @Enumerated(EnumType.STRING)
    private DocType type;

    private boolean verified;

    private boolean deleted;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

}
