package com.job.jobhunter.repository;

import com.job.jobhunter.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long>,
        JpaSpecificationExecutor<Permission> {

    boolean existsByApiPathAndMethodAndModule(String apiPath, String method, String module);
    List<Permission> findByIdIn(List<Long> id);
}
