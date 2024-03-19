package com.trysixth.sixth.models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RateLimiter {
    @JsonProperty("error_payload_id")
    private String errorPayloadId = UUID.randomUUID().toString();
    @JsonProperty("id")
    private String id;
    @JsonProperty("route")
    private String route;
    @JsonProperty("interval")
    private double interval; // You may need to adjust the data type based on your requirements
    @JsonProperty("rate_limit")
    private int rateLimit;
    @JsonProperty("last_updated")
    private double lastUpdated;
    @JsonProperty("created_at")
    private double createdAt;
    @JsonProperty("rate_limit_type")
    private String rateLimitType = "ip address"; // Default value
    @JsonProperty("unique_id")
    private String uniqueId = "host"; // Default value
    @JsonProperty("rate_limit_by_rules")
    private Map<String, String> rateLimitByRules; // You may need to adjust the data type based on your requirements
    @JsonProperty("error_payload")
    private Map<String, Map<String, String>> errorPayload;
    @JsonProperty("is_active")
    private boolean isActive;

 
    // Getters and setters
    public String getErrorPayloadId() {
        return errorPayloadId;
    }

    public void setErrorPayloadId(String id) {
        this.errorPayloadId = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public double getInterval() {
        return interval;
    }

    public void setInterval(double interval) {
        this.interval = interval;
    }

    public int getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(int rateLimit) {
        this.rateLimit = rateLimit;
    }

    public double getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(double lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public double getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(double createdAt) {
        this.createdAt = createdAt;
    }

    public String getRateLimitType() {
        return rateLimitType;
    }

    public void setRateLimitType(String rateLimitType) {
        this.rateLimitType = rateLimitType;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Map<String, String> getRateLimitByRules() {
        return rateLimitByRules;
    }

    public Map<String, Map<String, String>> getErrorPayload() {
        return errorPayload;
    }

    public void setErrorPayload(Map<String, Map<String, String>> errorPayload) {
        this.errorPayload = errorPayload;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public static Map<String, Object> jsonObjectToMap(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        
        // Iterate through the keys of the JSONObject
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            
            // If the value is another JSONObject, recursively convert it to a Map
            if (value instanceof JSONObject) {
                value = jsonObjectToMap((JSONObject) value);
            }
            
            // Put the key-value pair into the map
            map.put(key, value);
        }
        
        return map;
    }

    
    @Override
    public String toString() {
    return "RateLimiter{" +
    "errorPayloadId=" + errorPayloadId +
    ", id=" + id +
    ", route=" + route +
    ", interval=" + interval +
    ", rateLimitType=" + rateLimitType +
    ", unique_id=" + uniqueId +
    ", lastUpdated=" + lastUpdated +
    ", isActive=" + isActive +
    ", rateLimityRule=" + rateLimitByRules +
    ", error_payload=" + errorPayload +
    '}';
    }
}