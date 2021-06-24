package com.github.remusselea.scentdb.repo;

import com.github.remusselea.scentdb.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
