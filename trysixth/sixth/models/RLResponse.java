package com.trysixth.sixth.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


public class RLResponse {
    @JsonProperty("response")
    private boolean response;
    
    // Getter and Setter for isActive
    public boolean getResponse() {
        return response;
    }
}
