package com.iplens.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDto {
    private ContinentDto continent;
    private CountryDto country;
    private Double latitude;
    private Double longitude;
}
