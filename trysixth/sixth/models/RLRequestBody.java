package com.trysixth.sixth.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RLRequestBody {
    @JsonProperty("route")
    private String route;
    @JsonProperty("interval")
    private double interval;
    @JsonProperty("rate_limit")
    private int rateLimit;
    @JsonProperty("unique_id")
    private String uniqueId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("is_active")
    private boolean isActive;

    // Getter and Setter for route
    public String getRoute() {
        return route;
    }
    
    public void setRoute(String route) {
        this.route = route;
    }

    // Getter and Setter for interval
    public double getInterval() {
        return interval;
    }
    
    public void setInterval(double interval) {
        this.interval = interval;
    }

    // Getter and Setter for rateLimit
    public int getRateLimit() {
        return rateLimit;
    }
    
    public void setRateLimit(int rateLimit) {
        this.rateLimit = rateLimit;
    }

    // Getter and Setter for uniqueId
    public String getUniqueId() {
        return uniqueId;
    }
    
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    // Getter and Setter for userId
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and Setter for isActive
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
