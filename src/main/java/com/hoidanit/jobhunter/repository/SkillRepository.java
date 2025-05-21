package com.hoidanit.jobhunter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hoidanit.jobhunter.domain.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {

    Optional<Skill> findById(Long id);

    Boolean existsByName(String name);
    List<Skill> findByIdIn(List<Long> reqSkills);

}
