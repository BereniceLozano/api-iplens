package com.iplens.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Country {
    @Id
    @Column(length = 5)
    private String code;   // MX, US, etc

    private String name;   // Mexico
}
