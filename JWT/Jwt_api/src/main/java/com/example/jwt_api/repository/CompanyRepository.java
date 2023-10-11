package com.example.jwt_api.repository;

import com.example.jwt_api.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
//    Optional<Company> findByCompanyName(String company);
}
