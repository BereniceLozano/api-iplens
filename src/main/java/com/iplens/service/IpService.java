package com.iplens.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.iplens.data.IpInfo;
import com.iplens.dto.IpRegistryResponse;
import com.iplens.repository.IpInfoRepository;

import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class IpService {

    private final IpInfoRepository repo;
    private final CatalogService catalogService;
    private final WebClient webClient;

    @Value("${ipregistry.api-key}")
    private String apiKey;

    @Value("${ipregistry.fields}")
    private String fields;

    public IpService(IpInfoRepository repo,
                     CatalogService catalogService,
                     WebClient ipRegistryWebClient) {
        this.repo = repo;
        this.catalogService = catalogService;
        this.webClient = ipRegistryWebClient;
    }

    /**
     * Lookup IP information reactively.
     * @param ip the IP to query
     * @return Mono<IpInfo>
     */
    @Transactional(rollbackFor = Exception.class)
public Mono<IpInfo> lookup(String ip) {

    // 1️⃣ Check if IP exists reactively
    return Mono.fromCallable(() -> repo.findByIp(ip))
            .flatMap(optionalIp -> optionalIp
                    .map(existing -> Mono.<IpInfo>error(new DuplicateKeyException("IP already exists")))
                    .orElseGet(() -> Mono.<IpInfo>empty())
            )
            // 2️⃣ If IP does not exist, call IPRegistry API reactively
            .switchIfEmpty(
                    webClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/{ip}")
                                    .queryParam("key", apiKey)
                                    .queryParam("fields", fields)
                                    .build(ip)
                            )
                            .retrieve()
                            .bodyToMono(IpRegistryResponse.class)
                            .flatMap(response -> {
                                if (response == null ||
                                        response.getLocation() == null ||
                                        response.getLocation().getContinent() == null ||
                                        response.getLocation().getCountry() == null) {
                                    return Mono.error(new RuntimeException("Invalid response from IP registry"));
                                }

                                var location = response.getLocation();
                                String continentCode = location.getContinent().getCode();
                                String continentName = location.getContinent().getName();
                                String countryCode = location.getCountry().getCode();
                                String countryName = location.getCountry().getName();

                                // 3️⃣ Ensure continent and country exist in catalogs (reactive)
                                return catalogService.ensureContinent(continentCode, continentName)
                                        .then(catalogService.ensureCountry(countryCode, countryName))
                                        // 4️⃣ Build and save IpInfo reactively
                                        .then(Mono.fromCallable(() -> {
                                            IpInfo info = new IpInfo();
                                            info.setIp(ip);
                                            info.setType(response.getType());
                                            info.setDomain(response.getConnection().getDomain());
                                            info.setDomainType(response.getConnection().getType());
                                            info.setContinent(continentCode);
                                            info.setCountry(countryCode);
                                            info.setLatitude(location.getLatitude());
                                            info.setLongitude(location.getLongitude());
                                            info.setIsThreat(response.getSecurity().getIsThreat());
                                            return repo.save(info);
                                        }));
                            })
            );
}


    // Return all IPs (blocking is fine for simple endpoint)
    public List<IpInfo> findAll() {
        return repo.findAll();
    }

    // Delete ip record by id
    public void delete(Long id) {
        repo.deleteById(id);
    }
}