package com.hoidanit.jobhunter.repository;

import com.hoidanit.jobhunter.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionRepository extends JpaRepository<Permission, Long>,
        JpaSpecificationExecutor<Permission> {

    boolean existsByApiPathAndMethodAndModule(String apiPath, String method, String module);
    // Define any custom query methods if needed
}
