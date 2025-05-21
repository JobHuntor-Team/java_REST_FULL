package com.hoidanit.jobhunter.service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hoidanit.jobhunter.domain.Company;
import com.hoidanit.jobhunter.domain.User;
import com.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import com.hoidanit.jobhunter.domain.response.ResUserDTO;
import com.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import com.hoidanit.jobhunter.domain.response.UserCreateDTO;
import com.hoidanit.jobhunter.domain.response.ResultPaginationDTO.MetaDTO;
import com.hoidanit.jobhunter.domain.response.UserCreateDTO.UserCompany;
import com.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, CompanyService companyService, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
        this.userRepository = userRepository;
    }

    public UserCreateDTO createNewUser(User user) {
        User newUser = this.handleCreateUser(user);
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setId(user.getId());
        userCreateDTO.setEmail(user.getEmail());
        userCreateDTO.setName(user.getName());
        userCreateDTO.setGender(user.getGender());
        userCreateDTO.setAddress(user.getAddress());
        userCreateDTO.setAge(user.getAge());
        userCreateDTO.setCreatedAt(user.getCreatedAt());
        UserCreateDTO.UserCompany userCompany = new UserCreateDTO.UserCompany();
        userCompany.setId(user.getCompany().getId());
        userCompany.setName(user.getCompany().getName());
        userCreateDTO.setCompany(userCompany);
        return userCreateDTO;

    }

    public void deleteUser(Long id) {
        if (!this.userRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy người dùng với id: " + id);
        }
        this.userRepository.deleteById(id);
    }

    public ResUserDTO getUser(Long id) {
        User userEntity = this.handleGetUserById(id);
        ResUserDTO userDTO = new ResUserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setName(userEntity.getName());
        userDTO.setGender(userEntity.getGender());
        userDTO.setAddress(userEntity.getAddress());
        userDTO.setAge(userEntity.getAge());
        userDTO.setCreatedAt(userEntity.getCreatedAt());
        userDTO.setUpdatedAt(userEntity.getUpdatedAt());
        if (userEntity.getCompany() != null) {
            ResUserDTO.CompanyUser userCompany = new ResUserDTO.CompanyUser();
            userCompany.setId(userEntity.getCompany().getId());
            userCompany.setName(userEntity.getCompany().getName());
            userDTO.setCompany(userCompany);
        } else {
            userDTO.setCompany(null);
        }
        return userDTO;
    }

    private User handleGetUserById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy người dùng với id: " + id);
        }
        return user.get();
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
                        item.getCreatedAt(),
                        new ResUserDTO.CompanyUser(
                                item.getCompany() != null ? item.getCompany().getId() : 0,
                                item.getCompany() != null ? item.getCompany().getName() : null)))
                .collect(Collectors.toList());

        rs.setResult(listUser);

        return rs;
    }

    public User handleGetUserByUserName(String username) {
        return this.userRepository.findByEmail(username);
    }

    public ResUpdateUserDTO updateUser(User user) {
        User updateUser = this.handleUpdateUser(user);

        ResUpdateUserDTO resUpdateUserDTO = new ResUpdateUserDTO();
        resUpdateUserDTO.setId(updateUser.getId());
        resUpdateUserDTO.setName(updateUser.getName());
        resUpdateUserDTO.setGender(updateUser.getGender());
        resUpdateUserDTO.setAddress(updateUser.getAddress());
        resUpdateUserDTO.setAge(updateUser.getAge());
        resUpdateUserDTO.setUpdatedAt(updateUser.getUpdatedAt());
        if (updateUser.getCompany() != null) {
            UserCompany userCompany = new UserCompany();
            userCompany.setId(updateUser.getCompany().getId());
            userCompany.setName(updateUser.getCompany().getName());
            resUpdateUserDTO.setCompany(userCompany);
        } else {
            resUpdateUserDTO.setCompany(null);
        }
        return resUpdateUserDTO;
    }

    private User handleUpdateUser(User updateUser) {
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
        if (updateUser.getCompany() != null) {
            Company company = this.companyService.getCompanyById(updateUser.getCompany().getId());
            if (company != null) {
                user.setCompany(company);
            } else {
                user.setCompany(null);
            }
        }
        this.userRepository.save(user);
        return user;
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

    private User handleCreateUser(User user) {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại trong hệ thống");
        }
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Company company = this.companyService.getCompanyById(user.getCompany().getId());
        if (company != null) {
            user.setCompany(company);
        } else {
            user.setCompany(null);
        }
        return this.userRepository.save(user);
    }
}
