package com.iplens.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityDto {
    @JsonProperty("is_threat")
    private Boolean isThreat;
}
