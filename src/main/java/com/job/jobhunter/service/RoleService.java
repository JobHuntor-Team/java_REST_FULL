package com.job.jobhunter.service;


import com.job.jobhunter.domain.Permission;
import com.job.jobhunter.domain.Role;
import com.job.jobhunter.domain.response.ResultPaginationDTO;
import com.job.jobhunter.repository.PermissionRepository;
import com.job.jobhunter.repository.RoleRepository;
import com.job.jobhunter.util.error.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private  final RoleRepository roleRepository;
    private  final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository,PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role createRole(Role role) throws IdInvalidException {
        if(this.roleRepository.existsByName(role.getName())) {
            throw new IdInvalidException("Role này đã tồn tại");
        }
        List<Long> permissionIds = role.getPermissions().stream()
                .map(permission -> permission.getId())
                .toList();

        List<Permission> permissions = permissionRepository.findByIdIn(permissionIds);
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    public Role updateRole(Role role) throws IdInvalidException {
        Optional<Role> roleOptional = this.roleRepository.findById(role.getId());
        if(roleOptional.isEmpty()) {
            throw new IdInvalidException("Role này không tồn tại");
        }
        Role existingRole = roleOptional.get();
        List<Long> permissionIds = role.getPermissions().stream()
                .map(permission -> permission.getId())
                .toList();

        List<Permission> permissions = permissionRepository.findByIdIn(permissionIds);
        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());
        existingRole.setPermissions(permissions);
        existingRole.setActive(role.isActive());
        existingRole.setPermissions(permissions);
        return roleRepository.save(existingRole);
    }

    public ResultPaginationDTO getAllRole(Specification<Role> spec, Pageable pageable) {
        Page<Role> roles = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.MetaDTO meta = new ResultPaginationDTO.MetaDTO();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPageSize(roles.getTotalPages());
        meta.setTotalItem(roles.getTotalElements());

        result.setMeta(meta);
        result.setResult(roles.getContent());

        return result;
    }

    public void deleteRole(Long id) throws IdInvalidException {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if(roleOptional.isEmpty()) {
            throw new IdInvalidException("Role này không tồn tại");
        }
        this.roleRepository.deleteById(id);
    }

    public Role getRoleById(Long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if(roleOptional.isEmpty()) {
            return null;
        }
        return roleOptional.get();
    }
}
