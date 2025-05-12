package com.hoidanit.jobhunter.service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hoidanit.jobhunter.domain.User;
import com.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import com.hoidanit.jobhunter.domain.response.ResUserDTO;
import com.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import com.hoidanit.jobhunter.domain.response.UserCreateDTO;
import com.hoidanit.jobhunter.domain.response.ResultPaginationDTO.MetaDTO;
import com.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserCreateDTO createNewUser(User user) {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại trong hệ thống");
        }
        this.userRepository.save(user);
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setId(user.getId());
        userCreateDTO.setEmail(user.getEmail());
        userCreateDTO.setName(user.getName());
        userCreateDTO.setGender(user.getGender());
        userCreateDTO.setAddress(user.getAddress());
        userCreateDTO.setAge(user.getAge());
        userCreateDTO.setCreatedAt(user.getCreatedAt());
        return userCreateDTO;

    }

    public void deleteUser(Long id) {
        if (!this.userRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy người dùng với id: " + id);
        }
        this.userRepository.deleteById(id);
    }

    public ResUserDTO getUser(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy người dùng với id: " + id);
        }
        User userEntity = user.get();
        ResUserDTO userDTO = new ResUserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setName(userEntity.getName());
        userDTO.setGender(userEntity.getGender());
        userDTO.setAddress(userEntity.getAddress());
        userDTO.setAge(userEntity.getAge());
        userDTO.setCreatedAt(userEntity.getCreatedAt());
        userDTO.setUpdatedAt(userEntity.getUpdatedAt());
        return userDTO;
    }

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        MetaDTO mt = new MetaDTO();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPageSize(pageUser.getTotalPages());
        mt.setTotalItem(pageUser.getTotalElements());

        rs.setMeta(mt);
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> new ResUserDTO(
                        item.getId(),
                        item.getEmail(),
                        item.getName(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getUpdatedAt(),
                        item.getCreatedAt()))
                .collect(Collectors.toList());

        rs.setResult(listUser);

        return rs;
    }

    public User handleGetUserByUserName(String username) {
        return this.userRepository.findByEmail(username);
    }

    public ResUpdateUserDTO handleUpdateUser(User updateUser) {
        Optional<User> userOptional = this.userRepository.findById(updateUser.getId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy người dùng với id: " + updateUser.getId());
        }
        User user = userOptional.get();
        user.setId(updateUser.getId());
        user.setName(updateUser.getName());
        user.setGender(updateUser.getGender());
        user.setAddress(updateUser.getAddress());
        user.setAge(updateUser.getAge());
        user.setUpdatedAt(updateUser.getUpdatedAt());
        this.userRepository.save(user);

        ResUpdateUserDTO resUpdateUserDTO = new ResUpdateUserDTO();
        resUpdateUserDTO.setId(user.getId());
        resUpdateUserDTO.setName(user.getName());
        resUpdateUserDTO.setGender(user.getGender());
        resUpdateUserDTO.setAddress(user.getAddress());
        resUpdateUserDTO.setAge(user.getAge());
        resUpdateUserDTO.setUpdatedAt(user.getUpdatedAt());
        return resUpdateUserDTO;
    }

    public void handleUpdateRefreshToken(String refreshToken, String email) {
        User user = this.handleGetUserByUserName(email);
        if (user == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng với email: " + email);
        } else {
            user.setRefreshToken(refreshToken);
            this.userRepository.save(user);
        }
    }

    public User getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }
}
