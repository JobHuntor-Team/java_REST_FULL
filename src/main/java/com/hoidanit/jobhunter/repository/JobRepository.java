package com.hoidanit.jobhunter.repository;

import java.util.List;

import com.hoidanit.jobhunter.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hoidanit.jobhunter.domain.Job;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
}
