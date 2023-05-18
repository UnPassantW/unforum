package com.unpassant.unforum.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class GithubUser {
    private Long id;
    private String name;
    private String bio;



}
