package com.ecommerce.common.dto;

import com.ecommerce.common.constant.Gender;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserInfoDto {
    private Long userInfoId;
    private Long userId;
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Full Name is required")
    @Size(min = 3, max = 20, message = "Full Name length should be min 3 and max 40 characters")
    private String fullName;
    @NotBlank(message = "Mobile number is required")
    @Size(min = 10, max = 10, message = "Mobile number should not be greater than 10 digit")
    private String mobile;
    @NotBlank(message = "Street is required")
    @Size(min = 3, max = 128, message = "Street value should be min 3 and max 128 characters")
    private String street;
    @NotBlank(message = "Lane is required")
    @Size(min = 3, max = 128, message = "Lane value should be min 3 and max 128 characters")
    private String lane;
    @NotBlank(message = "State is required")
    @Size(min = 3, max = 128, message = "State value should be min 3 and max 128 characters")
    private String state;
    @NotBlank(message = "Country is required")
    @Size(min = 3, max = 128, message = "Country value should be min 3 and max 128 characters")
    private String country;
    @NotNull(message = "Pin code is required")
    @Min(value = 100000, message = "Pin code must be at least 6 digits long")
    @Max(value = 999999, message = "Pin code cannot exceed 6 digits")
    private Integer pincode;
    @NotBlank(message = "Gender is required")
    private String gender;
    private String profileUrl;
    private byte[] profilePic;
}
