package com.hoidanit.jobhunter.controller;


import com.hoidanit.jobhunter.domain.Permission;
import com.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import com.hoidanit.jobhunter.service.PermissionService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        return ResponseEntity.ok().body( this.permissionService.createPermission(permission));
    }

    @PutMapping("/permissions")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission permission) {
        return ResponseEntity.ok().body( this.permissionService.updatePermisson(permission));
    }

    @GetMapping("/permissions")
    public ResponseEntity<ResultPaginationDTO> getAllPermission(@Filter Specification<Permission> spec, Pageable pageable) {
        return ResponseEntity.ok().body( this.permissionService.getAllPermission(spec, pageable));
    }
}
