package com.mikkimesser.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTiny {
    private Integer id;
    private String name;
    private String job;
}
