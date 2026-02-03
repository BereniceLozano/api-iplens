package com.iplens.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IpRegistryResponse {
    private String type;
    private ConnectionDto connection;
    private LocationDto location;
    private SecurityDto security;
}
