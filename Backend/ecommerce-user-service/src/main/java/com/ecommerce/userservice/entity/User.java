package com.ecommerce.userservice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(
        name = "users",
        uniqueConstraints= @UniqueConstraint(columnNames={"username"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;
    private String Role;
    private boolean active;
    private boolean deleted;
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="user_info_id")
    private UserInfo userInfo;
    @OneToMany(mappedBy = "user")
    private List<Document> documents;
}
