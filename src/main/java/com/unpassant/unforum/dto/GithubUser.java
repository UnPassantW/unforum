package com.unpassant.unforum.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;



}
