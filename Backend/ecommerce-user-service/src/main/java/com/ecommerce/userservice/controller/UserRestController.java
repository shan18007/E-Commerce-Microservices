package com.ecommerce.userservice.controller;

import com.ecommerce.common.dto.Filter;
import com.ecommerce.common.dto.User;
import com.ecommerce.common.dto.UserSearchData;
import com.ecommerce.userservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(UserRestController.class);

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody @Valid User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody @Valid User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<User>> getAllUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.getAllUser(PageRequest.of(page, size)));
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAllUserById(
            @RequestBody List<Long> userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.getAllUsersById(userId, PageRequest.of(page, size)));
    }

    @GetMapping("/active/")
    public ResponseEntity<List<User>> getAllActiveNonDeletedUserById(
            @RequestBody List<Long> userId
    ) {
        return ResponseEntity.ok(userService.getAllActiveNonDeletedUsersById(userId));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<UserSearchData>> getFilterUserData(
            @RequestBody List<@Valid Filter> filters,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws NoSuchFieldException {
        return ResponseEntity.ok(userService.getUserFilterData(filters, PageRequest.of(page, size)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(
            @PathVariable("userId")
            @Positive(message = "userId must be a positive number")
            @NumberFormat(style = NumberFormat.Style.NUMBER)
            Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> removeUser(
            @PathVariable("userId")
            @Positive(message = "userId must be a positive number")
            @NumberFormat(style = NumberFormat.Style.NUMBER)
            Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User Successfully Deleted");
    }

}
