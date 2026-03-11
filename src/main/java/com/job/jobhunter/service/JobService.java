package com.job.jobhunter.service;

import com.job.jobhunter.domain.Job;
import com.job.jobhunter.domain.Skill;
import com.job.jobhunter.domain.response.ResCreateJob;
import com.job.jobhunter.domain.response.ResultPaginationDTO;
import com.job.jobhunter.repository.JobRepository;
import com.job.jobhunter.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public ResCreateJob handleCreateJob(Job job) {
        Job jobCreated = this.createJob(job);
        ResCreateJob resCreateJob = new ResCreateJob();
        resCreateJob.setName(jobCreated.getName());
        resCreateJob.setLocation(jobCreated.getLocation());
        resCreateJob.setSalary(jobCreated.getSalary());
        resCreateJob.setQuantity(jobCreated.getQuantity());
        resCreateJob.setLevel(jobCreated.getLevel());
        resCreateJob.setDescription(jobCreated.getDescription());
        resCreateJob.setStartDate(jobCreated.getStartDate());
        resCreateJob.setEndDate(jobCreated.getEndDate());
        resCreateJob.setActive(jobCreated.isActive());
        if (jobCreated.getSkills() != null) {
            List<String> skills = jobCreated.getSkills().stream()
                    .map(item -> item.getName()).collect(
                            Collectors.toList());
            resCreateJob.setSkillIds(skills);
            ;
        }

        return resCreateJob;
    }

    private Job createJob(Job job) {
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream().map(Skill::getId)
                    .toList();

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }
        return this.jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        this.jobRepository.deleteById(id);
    }

    public ResCreateJob handleUpdateJob(Job job) {
        Job updatedJob = this.updateJob(job);
        ResCreateJob resCreateJob = new ResCreateJob();
        resCreateJob.setName(updatedJob.getName());
        resCreateJob.setLocation(updatedJob.getLocation());
        resCreateJob.setSalary(updatedJob.getSalary());
        resCreateJob.setQuantity(updatedJob.getQuantity());
        resCreateJob.setLevel(updatedJob.getLevel());
        resCreateJob.setDescription(updatedJob.getDescription());
        resCreateJob.setStartDate(updatedJob.getStartDate());
        resCreateJob.setEndDate(updatedJob.getEndDate());
        resCreateJob.setActive(updatedJob.isActive());
        if (updatedJob.getSkills() != null) {
            List<String> skills = updatedJob.getSkills().stream()
                    .map(item -> item.getName()).collect(
                            Collectors.toList());
            resCreateJob.setSkillIds(skills);
        }
        return resCreateJob;
    }

    private Job updateJob(Job job) {
        Optional<Job> jobOptional = this.jobRepository.findById(job.getId());
        if (jobOptional.isEmpty()) {
            throw new IllegalArgumentException("Job not found");
        }
        Job current = jobOptional.get();
        if (job.getSkills() != null) {
            List<Long> updateSkills = job.getSkills()
                    .stream().map(Skill::getId)
                    .toList();

            List<Skill> dbSkills = this.skillRepository.findByIdIn(updateSkills);
            current.setSkills(dbSkills);
            ;
        }
        return this.jobRepository.save(current);
    }

    public ResultPaginationDTO getAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> pagJob = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.MetaDTO mt = new ResultPaginationDTO.MetaDTO();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPageSize(pagJob.getTotalPages());
        mt.setTotalItem(pagJob.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pagJob.getContent());
        return rs;
    }

    public Job handleGetJobById(Long id) {
        Optional<Job> job = this.jobRepository.findById(id);
        if (job.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy công việc với id: " + id);
        }
        return job.get();
    }
}

