package com.iplens.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class IpInfo {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String ip;

    private String type;          
    private String domain;        
    private String domainType;    
    private String continent;     // Continent.code
    private String country;       // Country.code
    private Double latitude;        
    private Double longitude;
    private Boolean isThreat;     
}
