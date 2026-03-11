package com.job.jobhunter.controller;

import com.job.jobhunter.domain.Role;
import com.job.jobhunter.domain.response.ResultPaginationDTO;
import com.job.jobhunter.service.RoleService;
import com.job.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Role role) throws IdInvalidException {
        return ResponseEntity.ok().body(this.roleService.createRole(role));
    }

    @PutMapping("/roles")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) throws IdInvalidException {
        return ResponseEntity.ok().body(this.roleService.updateRole(role));
    }

    @GetMapping("/roles")
    public ResponseEntity<ResultPaginationDTO> getRole(@Filter Specification<Role> spec, Pageable pageable) throws IdInvalidException {
        ResultPaginationDTO rs = this.roleService.getAllRole(spec, pageable);
        return ResponseEntity.ok().body(rs);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Long id) throws IdInvalidException {
        this.roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") Long id) throws IdInvalidException {
        Role role = this.roleService.getRoleById(id);
        return ResponseEntity.ok().body(role);
    }
}
