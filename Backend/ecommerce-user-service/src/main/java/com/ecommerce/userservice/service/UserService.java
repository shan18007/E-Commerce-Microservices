package com.ecommerce.userservice.service;

import com.ecommerce.common.dto.Filter;
import com.ecommerce.common.dto.User;
import com.ecommerce.common.dto.UserSearchData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public User addUser(User user);
    public User updateUser(User user);
    public void deleteUser(Long userId);
    public User getUser(Long userId);

    Page<User> getAllUsersById(List<Long> userId, Pageable page);

    List<User> getAllActiveNonDeletedUsersById(List<Long> userId);

    public Page<User> getAllUser(Pageable pageable);

    public Optional<com.ecommerce.userservice.entity.User> getUserByUserId(Long userId);

    List<UserSearchData> getUserFilterData(List<Filter> filters, Pageable pageable) throws NoSuchFieldException;

    public void isUsernameExist(String username);
    public void isEmailExist(String email);
    public Optional<com.ecommerce.userservice.entity.UserInfo> getUserByEmail(String email);
    public Optional<com.ecommerce.userservice.entity.User> getUserByUsername(String username);

}
