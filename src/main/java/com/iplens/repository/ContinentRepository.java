package com.iplens.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iplens.data.Continent;

public interface ContinentRepository extends JpaRepository<Continent, String> {
    
}
