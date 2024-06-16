package com.ecommerce.userservice.helper;

import com.ecommerce.common.dto.UserInfoDto;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.entity.UserInfo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class UserHelper {

    public static User toEntity(com.ecommerce.common.dto.User user) {
        User userEntity = User.builder().build();
        BeanUtils.copyProperties(user, userEntity);

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user.getUserInfo(), userInfo);
        userEntity.setUserInfo(userInfo);
        return userEntity;
    }

    public static List<User> toEntityList(List<com.ecommerce.common.dto.User> userList) {
        List<User> userEntityList = new ArrayList<>();
        userList.forEach(user -> userEntityList.add(toEntity(user)));
        return userEntityList;
    }

    public static com.ecommerce.common.dto.User toDto(User user) {
        com.ecommerce.common.dto.User userDto = com.ecommerce.common.dto.User.builder().build();
        BeanUtils.copyProperties(user, userDto);

        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(user.getUserInfo(), userInfoDto);
        userDto.setUserInfo(userInfoDto);

        return userDto;
    }

    public static List<com.ecommerce.common.dto.User> toDtoList(List<User> userList) {
        List<com.ecommerce.common.dto.User> userDtoList = new ArrayList<>();
        userList.forEach(user -> userDtoList.add(toDto(user)));
        return userDtoList;
    }

}
