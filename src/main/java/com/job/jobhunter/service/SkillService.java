package com.job.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.job.jobhunter.domain.Skill;
import com.job.jobhunter.domain.response.ResultPaginationDTO;
import com.job.jobhunter.domain.response.ResultPaginationDTO.MetaDTO;
import com.job.jobhunter.repository.SkillRepository;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill handleCreateSkill(Skill skill) {
        if (this.skillRepository.existsByName(skill.getName())) {
            throw new IllegalArgumentException("Skill đã tồn tại");
        }
        this.skillRepository.save(skill);
        return skill;
    }

    public Skill handleUpdateSkill(Skill skill) {
        Optional<Skill> skillOptional = this.skillRepository.findById(skill.getId());
        if (skillOptional.isEmpty()) {
            throw new IllegalArgumentException("Skill không tồn tại");
        }
        Skill updateSkill = skillOptional.get();
        updateSkill.setName(skill.getName());
        this.skillRepository.save(updateSkill);
        return updateSkill;
    }

    public ResultPaginationDTO handlefetchAllSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> skillPage = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        MetaDTO mt = new MetaDTO();



        return rs;
    }

    public void handleDeleteSkill(Long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        if (skillOptional.isEmpty()) {
            throw new IllegalArgumentException("Skill không tồn tại");
        }
        // xóa skill trong job
        Skill skill = skillOptional.get();
        skill.getJobs()
                .forEach(job -> job.getSkills()
                        .remove(skill));
        // xóa skill
        this.skillRepository.delete(skillOptional.get());
    }
}
