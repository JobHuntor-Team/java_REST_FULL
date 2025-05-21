package com.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hoidanit.jobhunter.domain.Skill;
import com.hoidanit.jobhunter.domain.response.ResUserDTO;
import com.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import com.hoidanit.jobhunter.domain.response.ResultPaginationDTO.MetaDTO;
import com.hoidanit.jobhunter.repository.SkillRepository;

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

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPageSize(skillPage.getTotalPages());
        mt.setTotalItem(skillPage.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(skillPage.getContent());

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
