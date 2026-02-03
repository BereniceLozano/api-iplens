package com.iplens.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iplens.data.Country;

public interface CountryRepository extends JpaRepository<Country, String> {
    
}
