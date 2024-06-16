package com.ecommerce.userservice.serviceimpl;

import com.ecommerce.common.constant.Role;
import com.ecommerce.common.dto.Filter;
import com.ecommerce.common.dto.User;
import com.ecommerce.common.dto.UserInfoDto;
import com.ecommerce.common.dto.UserSearchData;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.userservice.entity.UserInfo;
import com.ecommerce.userservice.external.services.DocumentServiceClient;
import com.ecommerce.userservice.helper.UserHelper;
import com.ecommerce.userservice.repo.DocumentRepository;
import com.ecommerce.userservice.repo.UserSearchFilterRepository;
import com.ecommerce.userservice.repo.UserInfoRepository;
import com.ecommerce.userservice.repo.UserRepository;
import com.ecommerce.userservice.service.UserService;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserInfoRepository userInfoRepository;

    private DocumentRepository documentRepository;
    private UserSearchFilterRepository dynamicFilterRepository;

    private DocumentServiceClient documentServiceClient;

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, UserInfoRepository userInfoRepository
        ,DocumentRepository documentRepository, UserSearchFilterRepository dynamicFilterRepository,
                           DocumentServiceClient documentServiceClient
    ) {
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.documentRepository = documentRepository;
        this.dynamicFilterRepository = dynamicFilterRepository;
        this.documentServiceClient = documentServiceClient;
    }

    @Transactional
    @Override
    public User addUser(User user) {
        com.ecommerce.userservice.entity.User userEntity = UserHelper.toEntity(user);
        LOG.info("Saving User - {}", userEntity);

        isUsernameExist(userEntity.getUsername());
        isEmailExist(userEntity.getUserInfo().getEmail());

        userEntity.setRole(userEntity.getRole().isEmpty() ? Role.USER.name() : userEntity.getRole());

        // Save Password in encrypted format

        com.ecommerce.userservice.entity.User saveUser = userRepository.save(userEntity);
        return UserHelper.toDto(saveUser);
    }

    @Override
    public User updateUser(User user) {
        LOG.info("Updating User - {}", user);
        Optional<com.ecommerce.userservice.entity.User> existingUser = getUserByUserId(user.getUserId());
        UserInfo existingUserInfo = existingUser.get().getUserInfo();
        UserInfoDto userInfo = user.getUserInfo();

        isEmailExist(existingUserInfo.getEmail());

        // Only Below info is allowed to update
        existingUserInfo.setEmail(userInfo.getEmail());
        existingUserInfo.setCountry(userInfo.getCountry());
        existingUserInfo.setLane(userInfo.getLane());
        existingUserInfo.setMobile(userInfo.getMobile());
        existingUserInfo.setPincode(userInfo.getPincode());
        existingUserInfo.setProfileUrl(userInfo.getProfileUrl());
        existingUserInfo.setState(userInfo.getState());
        existingUserInfo.setStreet(userInfo.getStreet());
        existingUser.get().setUserInfo(existingUserInfo);

        User updatedUser = UserHelper.toDto(userRepository.save(existingUser.get()));
        LOG.info("User Updated - {}", updatedUser);
        return updatedUser;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User getUser(Long userId) {
        return UserHelper.toDto(getUserByUserId(userId).get());
    }

    @Override
    public Optional<com.ecommerce.userservice.entity.User> getUserByUserId(Long userId) {
        return Optional.ofNullable(userRepository.findById(userId).orElseThrow(() -> new BusinessException("Invalid userId")));
    }

    @Override
    public Page<User> getAllUsersById(List<Long> userId, Pageable page) {
        List<User> dtoList = UserHelper.toDtoList(userRepository.getAllUsersByUserId(userId, page).getContent());
        return new PageImpl<>(dtoList);
    }

    @Override
    public List<User> getAllActiveNonDeletedUsersById(List<Long> userId) {
        return UserHelper.toDtoList(userRepository.getAllActiveNonDeletedUsers(userId));
    }

    @Override
    public Page<User> getAllUser(Pageable pageable)  {
        LOG.info("Fetching All users from db");
        Page<com.ecommerce.userservice.entity.User> usersPage = userRepository.findAll(pageable);
        return new PageImpl<>(UserHelper.toDtoList(usersPage.getContent()));

    }
    @Override
    public List<UserSearchData> getUserFilterData(List<Filter> filters, Pageable pageable) throws NoSuchFieldException {
        List<UserSearchData> userSearchData = dynamicFilterRepository.applyDynamicFilter(filters);
        LOG.info("User filter Data - {}", userSearchData);
        return userSearchData;

    }

    @Override
    public void isUsernameExist(String username) {
        if(StringUtils.isEmpty(username)) {
            LOG.info("Username is empty - {}",username);
           return;
        }
        getUserByUsername(username).ifPresent((user) -> new BusinessException("Please user another username"));
    }

    @Override
    public void isEmailExist(String email) {
        if(StringUtils.isEmpty(email)) {
            LOG.info("Email is empty - {}",email);
           return;
        }
        getUserByEmail(email).ifPresent((user) -> new BusinessException("Please user another Email"));
    }

    @Override
    public Optional<com.ecommerce.userservice.entity.UserInfo> getUserByEmail(String email) {
        return userInfoRepository.findUserInfoByEmail(email);
    }

    @Override
    public Optional<com.ecommerce.userservice.entity.User> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

}
