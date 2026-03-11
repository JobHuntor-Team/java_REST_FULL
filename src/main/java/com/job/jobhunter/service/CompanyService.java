package com.job.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.job.jobhunter.domain.Company;
import com.job.jobhunter.domain.response.ResultPaginationDTO;
import com.job.jobhunter.domain.response.ResultPaginationDTO.MetaDTO;
import com.job.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public ResultPaginationDTO handleAllCompanies(Pageable pageable, Specification<Company> spec) {
        Page<Company> companyPage = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        MetaDTO metaDTO = new MetaDTO();
        metaDTO.setPage(pageable.getPageNumber() + 1);
        metaDTO.setPageSize(pageable.getPageSize());
        metaDTO.setTotalPage(companyPage.getTotalPages());
        metaDTO.setTotalItem(companyPage.getTotalElements());
        resultPaginationDTO.setMeta(metaDTO);
        resultPaginationDTO.setResult(companyPage.getContent());
        return resultPaginationDTO;
    }

    public void deleteCompany(Long companyId) {
        this.companyRepository.deleteById(companyId);
    }

    public Company updateCompany(Company company) {
        Optional<Company> companyOptional = this.companyRepository.findById(company.getId());
        if (companyOptional.isPresent()) {
            Company existingCompany = companyOptional.get();
            existingCompany.setName(company.getName());
            existingCompany.setAddress(company.getAddress());
            existingCompany.setDescription(company.getDescription());
            existingCompany.setLogo(company.getLogo());
            return this.companyRepository.save(existingCompany);
        } else {
            throw new RuntimeException("Company not found with id: " + company.getId());
        }
    }

    public Company getCompanyById(Long companyId) {
        return this.companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));
    }
}
