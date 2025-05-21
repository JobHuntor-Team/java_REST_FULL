package com.hoidanit.jobhunter.controller;


import com.hoidanit.jobhunter.domain.Resume;
import com.hoidanit.jobhunter.domain.User;
import com.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import com.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import com.hoidanit.jobhunter.domain.response.resume.ResResumesDTO;
import com.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import com.hoidanit.jobhunter.service.ResumeService;
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
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    public ResponseEntity<ResCreateResumeDTO> createResume(@RequestBody Resume resume) {
        return ResponseEntity.ok().body(this.resumeService.handleCreateResume(resume));
    }

    @PutMapping("/resumes")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) {
        return ResponseEntity.ok().body(this.resumeService.handleUpdateResume(resume));
    }

    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") Long id) {
        this.resumeService.handleDeleteResume(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/resumes/{id}")
    public ResponseEntity<ResResumesDTO> getResumeById(@PathVariable("id") Long id) {
        ResResumesDTO resResumesDTO = this.resumeService.handleGetResumeById(id);
        return ResponseEntity.ok().body(resResumesDTO);
    }

    @GetMapping("/resumes")
    public ResponseEntity<ResultPaginationDTO> getAllResumes(@Filter Specification<Resume> spec, Pageable pageable) {

        return ResponseEntity.ok().body(this.resumeService.handleGetAllResumes(spec, pageable));
    }
}
