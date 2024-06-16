package com.ecommerce.userservice.entity;

import com.ecommerce.common.constant.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "user_info",
        uniqueConstraints= @UniqueConstraint(columnNames={"email"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userInfoId;
    @OneToOne(mappedBy="userInfo", cascade=CascadeType.ALL)
    private User user;
    private String email;
    private String fullName;
    private String mobile;
    private String street;
    private String lane;
    private String state;
    private String country;
    private Integer pincode;
    private String gender;
    private String profileUrl;
}
