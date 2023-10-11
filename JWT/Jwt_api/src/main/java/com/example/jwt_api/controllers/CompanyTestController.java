package com.example.jwt_api.controllers;


import com.example.jwt_api.models.Company;
import com.example.jwt_api.repository.CompanyRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class CompanyTestController {

    @Autowired
    CompanyRepository companyRepository;

    @GetMapping("/companies")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Company>> getAllCompany() {
        List<Company> companies = new ArrayList<>();
        companyRepository.findAll().forEach(companies::add);
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @PostMapping(value = "/addCompany")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Company> addCompany(@Valid @ModelAttribute Company company, @RequestParam("imageCompany") MultipartFile multipartFile) {
        try {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            company.setLogoCompany(fileName);
            Company _company = new Company(company.getName(), company.getEmail(),
                    company.getDescription(), company.getAddress(), company.getPhoneNum(), company.getLogoCompany());
            Company saved = companyRepository.save(_company);

            String uploadDir = "./images_company/" + saved.getId();

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/company/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Company> getCompanyById(@PathVariable("id") long id) {
        Optional<Company> companyData = companyRepository.findById(id);
        if (companyData.isPresent()) {
            return new ResponseEntity<>(companyData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/editCompany/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Company> editCompany(@PathVariable("id") long id, @RequestBody Company company) {
        Optional<Company> companyData = companyRepository.findById(id);

        if (companyData.isPresent()) {
            Company _company = companyData.get();
            _company.setName(company.getName());
            _company.setEmail(company.getEmail());
            _company.setDescription(company.getDescription());
            _company.setAddress(company.getAddress());
            _company.setPhoneNum(company.getPhoneNum());
            _company.setLogoCompany(company.getLogoCompany());
            return new ResponseEntity<>(companyRepository.save(_company), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/company/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")

    public ResponseEntity<Company> deleteCompany(@PathVariable("id") long id) {
        try {
            companyRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
