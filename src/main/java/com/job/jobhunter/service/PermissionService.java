package com.job.jobhunter.service;


import com.job.jobhunter.domain.Permission;
import com.job.jobhunter.domain.response.ResultPaginationDTO;
import com.job.jobhunter.repository.PermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionService {

    private  final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission createPermission(Permission permission) {
        if (this.permissionRepository.existsByApiPathAndMethodAndModule(permission.getApiPath(), permission.getMethod(), permission.getModule())) {
            throw new IllegalArgumentException("Permission này đã tồn tại");
        }
        return  this.permissionRepository.save(permission);
    }

    public Permission updatePermisson(Permission permission) {
        Optional<Permission> existingPermission = this.permissionRepository.findById(permission.getId());
        if (existingPermission.isEmpty()) {
            throw new IllegalArgumentException("Permission này không tồn tại");
        }

        if (!this.permissionRepository.existsByApiPathAndMethodAndModule(permission.getApiPath(), permission.getMethod(), permission.getModule())) {
            throw new IllegalArgumentException("Permission này đã tồn tại");
        }
        // Update the existing permission with new values
        Permission existing = existingPermission.get();
        existing.setName(permission.getName());
        existing.setApiPath(permission.getApiPath());
        existing.setMethod(permission.getMethod());
        existing.setModule(permission.getModule());

        return  this.permissionRepository.save(existing);
    }

    public ResultPaginationDTO getAllPermission(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> permissions = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.MetaDTO mt = new ResultPaginationDTO.MetaDTO();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPageSize(permissions.getTotalPages());
        mt.setTotalItem(permissions.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(permissions.getContent());
        return rs;
    }

    public void deletePermission(Long id) {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        if (permissionOptional.isEmpty()) {
            throw new IllegalArgumentException("Permission này không tồn tại");
        }
        Permission permission = permissionOptional.get();
        permission.getRoles().stream().forEach(role -> {
            role.getPermissions().remove(permission);
        });
        this.permissionRepository.delete(permissionOptional.get());
    }

}
