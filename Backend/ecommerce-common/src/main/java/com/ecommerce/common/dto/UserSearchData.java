package com.ecommerce.common.dto;

import com.ecommerce.common.constant.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class UserSearchData {
    private Long userId;
    private String username;
    private String Role;
    private Boolean active;
    private Long userInfoId;
    private String country;
    private String email;
    private String fullName;
    private String gender;
    private String lane;
    private String mobile;
    private String street;
    private String state;
    private Integer pincode;
    private String profileUrl;

    // [java.lang.Long, java.lang.String, java.lang.String, java.lang.Boolean, java.lang.Long, java.lang.String, java.lang.String, java.lang.String, com.ecommerce.common.constant.Gender, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String]

    public UserSearchData(Long userId, String username, String role, Boolean active, Long userInfoId, String country, String email, String fullName, String gender, String lane, String mobile, String street, String state, Integer pincode, String profileUrl) {
        this.userId = userId;
        this.username = username;
        this.Role = role;
        this.active = active;
        this.userInfoId = userInfoId;
        this.country = country;
        this.email = email;
        this.fullName = fullName;
        this.gender = gender;
        this.lane = lane;
        this.mobile = mobile;
        this.street = street;
        this.state = state;
        this.pincode = pincode;
        this.profileUrl = profileUrl;
    }
}

