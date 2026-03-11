package com.job.jobhunter.service;

import com.job.jobhunter.domain.Company;
import com.job.jobhunter.domain.Role;
import com.job.jobhunter.domain.User;
import com.job.jobhunter.domain.response.ResUpdateUserDTO;
import com.job.jobhunter.domain.response.ResUserDTO;
import com.job.jobhunter.domain.response.ResultPaginationDTO;
import com.job.jobhunter.domain.response.ResultPaginationDTO.MetaDTO;
import com.job.jobhunter.domain.response.UserCreateDTO;
import com.job.jobhunter.domain.response.UserCreateDTO.UserCompany;
import com.job.jobhunter.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository,
                       CompanyService companyService,
                       PasswordEncoder passwordEncoder,
                       RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
        this.userRepository = userRepository;
        this.roleService = roleService;
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
        UserCreateDTO.RoleUserDTO roleUserDTO = new UserCreateDTO.RoleUserDTO();
        roleUserDTO.setId(user.getRole().getId());
        roleUserDTO.setName(user.getRole().getName());
        roleUserDTO.setActive(user.getRole().isActive());
        userCreateDTO.setRoleUserDTO(roleUserDTO);
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
                                item.getCompany() != null ? item.getCompany().getName() : null)
                                , item.getRole() != null ? new UserCreateDTO.RoleUserDTO(
                                        item.getRole().getId(),
                                        item.getRole().getName(),
                                        item.getRole().isActive()) : null))
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
        if (updateUser.getRole() != null) {
            UserCreateDTO.RoleUserDTO roleUserDTO = new UserCreateDTO.RoleUserDTO();
            roleUserDTO.setId(updateUser.getRole().getId());
            roleUserDTO.setName(updateUser.getRole().getName());
            roleUserDTO.setActive(updateUser.getRole().isActive());
            resUpdateUserDTO.setRoleUserDTO(roleUserDTO);
        } else {
            resUpdateUserDTO.setRoleUserDTO(null);
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
        if (updateUser.getRole() != null) {
            Role role = this.roleService.getRoleById(updateUser.getRole().getId());
            user.setRole(role != null ? role : null);
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

        Role role = this.roleService.getRoleById(user.getRole().getId());
        user.setRole(role != null ? role : null);
        return this.userRepository.save(user);
    }
}
