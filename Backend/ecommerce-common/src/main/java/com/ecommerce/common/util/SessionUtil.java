package com.ecommerce.common.util;

import com.ecommerce.common.constant.Role;
import com.ecommerce.common.dto.User;
import com.ecommerce.common.dto.UserInfoDto;

public class SessionUtil {

    User user;
    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public String getUserName() {
      return user.getUsername();
    }

    public boolean isUserDeleted() {
        return user.isDeleted();
    }

    public boolean isUserActive() {
        return user.isActive();
    }

    public UserInfoDto getUserInfo() {
        return user.getUserInfo();
    }

    public boolean hasRole(Role role) {
        return user.getRole() == role.toString();
    }


}
