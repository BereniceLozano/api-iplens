package com.iplens.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iplens.data.Continent;
import com.iplens.data.Country;
import com.iplens.repository.ContinentRepository;
import com.iplens.repository.CountryRepository;

import reactor.core.publisher.Mono;

@Service
public class CatalogService {

    private final ContinentRepository continentRepo;
    private final CountryRepository countryRepo;

    public CatalogService(ContinentRepository continentRepo, CountryRepository countryRepo) {
        this.continentRepo = continentRepo;
        this.countryRepo = countryRepo;
    }

    /**
     * Ensure continent exists. If not, create it.
     */
    @Transactional
    public Mono<Continent> ensureContinent(String code, String name) {
        return Mono.fromCallable(() -> continentRepo.findById(code)
                .orElseGet(() -> {
                    Continent c = new Continent();
                    c.setCode(code);
                    c.setName(name);
                    return continentRepo.save(c);
                })
        );
    }

    /**
     * Ensure country exists. If not, create it.
     */
    @Transactional
    public Mono<Country> ensureCountry(String code, String name) {
        return Mono.fromCallable(() -> countryRepo.findById(code)
                .orElseGet(() -> {
                    Country c = new Country();
                    c.setCode(code);
                    c.setName(name);
                    return countryRepo.save(c);
                })
        );
    }

    /**
     * Retrieve catalog by param catalogName.
     */
    public Mono<List<?>> findCatalog(String catalogName) {

        return Mono.fromCallable(() -> {
            return switch (catalogName.toLowerCase()) {
                case "continent" -> continentRepo.findAll();
                case "country" -> countryRepo.findAll();
                default -> throw new IllegalArgumentException(
                        "Invalid catalog. Allowed values: continent, country"
                );
            };
        });
    }
}
