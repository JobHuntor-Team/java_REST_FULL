package com.job.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.job.jobhunter.domain.Company;
import com.job.jobhunter.domain.response.ResultPaginationDTO;
import com.job.jobhunter.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/createCompanies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        Company newCompany = this.companyService.createCompany(company);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(newCompany);
    }

    @GetMapping("/getCompanies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(@Filter Specification<Company> spec,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.companyService.handleAllCompanies(pageable, spec));
    }

    @DeleteMapping("/deleteCompanies/{companyId}")
    public ResponseEntity<String> deleteCompany(@PathVariable("companyId") Long companyId) {
        this.companyService.deleteCompany(companyId);
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).body("xoa thanh cong");
    }

    @PutMapping("/updateCompanies")
    public ResponseEntity<Company> putUpdateCompanies(@RequestBody Company Company) {
        Company updateCompany = this.companyService.updateCompany(Company);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(updateCompany);
    }
}
