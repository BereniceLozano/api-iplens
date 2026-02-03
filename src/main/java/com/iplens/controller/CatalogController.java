package com.iplens.controller;


import com.iplens.service.CatalogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {
    
    private final CatalogService service;

    public CatalogController(CatalogService service) {
        this.service = service;
    }

    
    @GetMapping
    @Operation(
        summary = "Obtener catálogos",
        description = "Devuelve un catálogo por nombre (continent, country)"
    )
    public Mono<List<?>> getCatalog(
            @Parameter(
                description = "Nombre de catálogo: continent | country",
                example = "continent",
                required = true
            )
            @RequestParam String catalog) {

        return service.findCatalog(catalog);
    }
}
