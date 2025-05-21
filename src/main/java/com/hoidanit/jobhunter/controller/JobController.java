package com.hoidanit.jobhunter.controller;

import com.hoidanit.jobhunter.domain.Job;
import com.hoidanit.jobhunter.domain.response.ResCreateJob;
import com.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import com.hoidanit.jobhunter.service.JobService;
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
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    public ResponseEntity<ResCreateJob> createJob(@RequestBody Job job) {

        ResCreateJob resCreateJob = this.jobService.handleCreateJob(job);
        return ResponseEntity.ok().body(resCreateJob);
    }

    @GetMapping("/jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification< Job > spec, Pageable
            pageable) {
        ResultPaginationDTO resultPaginationDTO = this.jobService.getAllJobs(spec, pageable);
        return ResponseEntity.ok().body(resultPaginationDTO);
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable("id") Long id) {
        this.jobService.deleteJob(id);
        return ResponseEntity.ok("Delete job with id: " + id);
    }

    @PutMapping("/jobs")
    public ResponseEntity<ResCreateJob> updateJob(@RequestBody Job job) {
        ResCreateJob resCreateJob = this.jobService.handleUpdateJob(job);
        return ResponseEntity.ok().body(resCreateJob);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable("id") Long id) {
        Job job = this.jobService.handleGetJobById(id);
        return ResponseEntity.ok().body(job);
    }
}
