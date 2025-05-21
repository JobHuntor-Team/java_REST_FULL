package com.hoidanit.jobhunter.repository;


import com.hoidanit.jobhunter.domain.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ResumeRepository extends JpaRepository<Resume, Long>, JpaSpecificationExecutor<Resume> {

}
