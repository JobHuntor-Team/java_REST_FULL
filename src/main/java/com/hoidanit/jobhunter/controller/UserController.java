package com.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.hoidanit.jobhunter.domain.User;
import com.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import com.hoidanit.jobhunter.domain.response.ResUserDTO;
import com.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import com.hoidanit.jobhunter.domain.response.UserCreateDTO;
import com.hoidanit.jobhunter.service.UserService;
import com.hoidanit.jobhunter.util.annotation.ApiMessage;
import com.hoidanit.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserCreateDTO> createNewUser(@RequestBody User postManUser) {
        postManUser.setPassword(this.passwordEncoder.encode(postManUser.getPassword()));
        UserCreateDTO newUser = this.userService.createNewUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete user by id")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        this.userService.deleteUser(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Get user by id")
    public ResponseEntity<ResUserDTO> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.userService.getUser(id));
    }

    @GetMapping("/users")
    @ApiMessage("Get all users")
    public ResponseEntity<ResultPaginationDTO> getUserByAll(
            @Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.userService.fetchAllUser(spec, pageable));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ResUpdateUserDTO> putUserById(@PathVariable("id") long id,
            @RequestBody User updateUser) {
        ResUpdateUserDTO resUpdateUserDTO = this.userService.handleUpdateUser(updateUser);
        return ResponseEntity.ok().body(resUpdateUserDTO);
    }
}
