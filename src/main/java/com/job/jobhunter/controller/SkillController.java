package com.job.jobhunter.controller;

import com.job.jobhunter.domain.Skill;
import com.job.jobhunter.domain.response.ResultPaginationDTO;
import com.job.jobhunter.service.SkillService;
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
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    public ResponseEntity<Skill> createSkill(@RequestBody Skill skill) {
        Skill existingSkill = this.skillService.handleCreateSkill(skill);
        return ResponseEntity.ok().body(existingSkill);
    }

    @PutMapping("/skills")
    public ResponseEntity<Skill> updateSkill(@RequestBody Skill skill) {
        Skill updateSkill = this.skillService.handleUpdateSkill(skill);
        return ResponseEntity.ok().body(updateSkill);
    }

    @GetMapping("/skills")
    public ResponseEntity<ResultPaginationDTO> getSkill(@Filter Specification<Skill> spec, Pageable pageable) {

        return ResponseEntity.ok().body(this.skillService.handlefetchAllSkill(spec, pageable));
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") Long id) {
        this.skillService.handleDeleteSkill(id);
        return ResponseEntity.ok().body(null);
    }
}
