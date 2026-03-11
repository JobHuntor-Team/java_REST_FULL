package com.job.jobhunter.service;

import com.job.jobhunter.domain.Job;
import com.job.jobhunter.domain.Resume;
import com.job.jobhunter.domain.User;
import com.job.jobhunter.domain.response.ResultPaginationDTO;
import com.job.jobhunter.domain.response.resume.ResCreateResumeDTO;
import com.job.jobhunter.domain.response.resume.ResResumesDTO;
import com.job.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import com.job.jobhunter.repository.JobRepository;
import com.job.jobhunter.repository.ResumeRepository;
import com.job.jobhunter.repository.UserRepository;
import com.job.jobhunter.util.SecurityUtil;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;


    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository
            , JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public ResCreateResumeDTO handleCreateResume(Resume resume) {
        Resume resumeCreate = createResume(resume);
        ResCreateResumeDTO resCreateResumeDTO = new ResCreateResumeDTO();
        resCreateResumeDTO.setId(resumeCreate.getId());
        resCreateResumeDTO.setCreatedAt(resumeCreate.getCreatedAt());
        resCreateResumeDTO.setCreatedBy(resumeCreate.getCreatedBy());
        return resCreateResumeDTO;
    }

    private Resume createResume(Resume resume) {
        Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        resume.setUser(userOptional.get());
        Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
        if (jobOptional.isEmpty()) {
            throw new IllegalArgumentException("Job not found");
        }
        resume.setJob(jobOptional.get());
        Resume resumeCreate = this.resumeRepository.save(resume);
        return resumeCreate;
    }

    public ResUpdateResumeDTO handleUpdateResume(Resume resume) {
        Resume resumeUpdate = updateResume(resume);
        ResUpdateResumeDTO resCreateResumeDTO = new ResUpdateResumeDTO();
        resCreateResumeDTO.setUpdatedAt(resumeUpdate.getUpdatedAt());
        resCreateResumeDTO.setUpdatedBy(resumeUpdate.getUpdatedBy());
        return resCreateResumeDTO;
    }

    private Resume updateResume(Resume resume) {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(resume.getId());
        if (resumeOptional.isEmpty()) {
            throw new IllegalArgumentException("Resume not found");
        }
        Resume resumeCurrent = resumeOptional.get();
        Resume resumeUpdate = new Resume();
        resumeUpdate.setId(resumeCurrent.getId());
        resumeUpdate.setStatus(resumeCurrent.getStatus());
        resumeUpdate.setEmail(resumeCurrent.getEmail());
        resumeUpdate.setUrl(resumeCurrent.getUrl());
        resumeUpdate.setCreatedAt(resumeCurrent.getCreatedAt());
        resumeUpdate.setCreatedBy(resumeCurrent.getCreatedBy());
        Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
        if (!userOptional.isEmpty()) {
            resumeUpdate.setUser(userOptional.get());
        }
        Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
        if (!jobOptional.isEmpty()) {
            resumeUpdate.setJob(jobOptional.get());
        }
        resumeUpdate.setCreatedAt(resumeCurrent.getCreatedAt());
        resumeUpdate.setCreatedBy(resumeCurrent.getCreatedBy());
        return this.resumeRepository.save(resumeUpdate);
    }

    public void handleDeleteResume(Long id) {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(id);
        if (resumeOptional.isEmpty()) {
            throw new IllegalArgumentException("Resume not found");
        }
        this.resumeRepository.delete(resumeOptional.get());
    }

    public ResResumesDTO handleGetResumeById(Long id) {
        Resume resume = GetResumeById(id);
        ResResumesDTO resResumesDTO = new ResResumesDTO();
        resResumesDTO.setId(resume.getId());
        resResumesDTO.setEmail(resume.getEmail());
        resResumesDTO.setUrl(resume.getUrl());
        resResumesDTO.setStatus(resume.getStatus().name());
        resResumesDTO.setCreatedAt(resume.getCreatedAt());
        resResumesDTO.setUpdatedAt(resume.getUpdatedAt());
        resResumesDTO.setCreatedBy(resume.getCreatedBy());
        resResumesDTO.setUpdatedBy(resume.getUpdatedBy());
        ResResumesDTO.UserResumeDTO userResumeDTO = new ResResumesDTO.UserResumeDTO();
        if (resume.getUser() != null) {
            userResumeDTO.setId(resume.getUser().getId());
            userResumeDTO.setName(resume.getUser().getName());
        }
        resResumesDTO.setUser(userResumeDTO);
        ResResumesDTO.JobResumeDTO jobResumeDTO = new ResResumesDTO.JobResumeDTO();
        if (resume.getJob() != null) {
            jobResumeDTO.setId(resume.getJob().getId());
            jobResumeDTO.setName(resume.getJob().getName());
        }
        resResumesDTO.setJob(jobResumeDTO);
        return  resResumesDTO;
    }

    private Resume GetResumeById(Long id) {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(id);
        if (resumeOptional.isEmpty()) {
            throw new IllegalArgumentException("Resume not found");
        }
        Resume resume = resumeOptional.get();
        return resume;
    }

    public ResultPaginationDTO handleGetAllResumes(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.MetaDTO mt = new ResultPaginationDTO.MetaDTO();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPageSize(pageResume.getTotalPages());
        mt.setTotalItem(pageResume.getTotalElements());

        rs.setMeta(mt);
        List<ResResumesDTO> listResume = pageResume.getContent()
                .stream().map(item -> new ResResumesDTO(
                        item.getId(),
                        item.getEmail(),
                        item.getUrl(),
                        item.getStatus().name(),
                        item.getUpdatedAt(),
                        item.getCreatedAt(),
                        item.getCreatedBy(),
                        item.getUpdatedBy(),
                        new ResResumesDTO.UserResumeDTO(
                                item.getUser() != null ? item.getUser().getId() : 0,
                                item.getUser() != null ? item.getUser().getName() : null),
                        new ResResumesDTO.JobResumeDTO(
                                item.getJob() != null ? item.getJob().getId() : 0,
                                item.getJob() != null ? item.getJob().getName() : null)))
                .collect(Collectors.toList());

        rs.setResult(listResume);

        return rs;
    }

    public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.MetaDTO mt = new ResultPaginationDTO.MetaDTO();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPageSize(pageResume.getTotalPages());
        mt.setTotalItem(pageResume.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResResumesDTO> listResume = pageResume.getContent()
                .stream().map(item -> this.handleGetResumeById(item.getId()))
                .collect(Collectors.toList());

        rs.setResult(listResume);

        return rs;
    }

}
