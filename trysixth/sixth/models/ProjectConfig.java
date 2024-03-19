package com.trysixth.sixth.models;

import java.util.Map;
import java.util.UUID;
import java.util.Map;
import java.util.Optional;
import java.util.Optional;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.trysixth.sixth.models.Encryption;
import com.trysixth.sixth.models.RateLimiter;



@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectConfig {
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("rate_limiter")
    private Map<String, RateLimiter> rate_limiter;
    @JsonProperty("encryption")
    private Encryption encryption;
    @JsonProperty("base_url")
    private String baseUrl;
    @JsonProperty("encryption_enabled")
    private Boolean encryptionEnabled;
    @JsonProperty("rate_limiter_enabled")
    private Boolean rateLimiterEnabled;
    @JsonProperty("last_updated")
    private Double lastUpdated;
    @JsonProperty("created_at")
    private Double createdAt;



    // Getters and setters
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
    this.userId = userId;
    }
    
    public Map<String, RateLimiter> getRateLimiter() {
    return rate_limiter;
    }
    
    public void setRateLimiter(Map<String, RateLimiter> rate_limiter) {
    this.rate_limiter = rate_limiter;
    }
    
    public Encryption getEncryption() {
    return encryption;
    }
    
    public void setEncryption(Encryption encryption) {
        this.encryption = encryption;
    }
    
    public String getBaseUrl() {
    return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
    }
    
    public Boolean isEncryptionEnabled() {
    return encryptionEnabled;
    }
    
    public void setEncryptionEnabled(Boolean encryptionEnabled) {
    this.encryptionEnabled = encryptionEnabled;
    }
    
    public Boolean isRateLimiterEnabled() {
    return rateLimiterEnabled;
    }
    
    public void setRateLimiterEnabled(Boolean rateLimiterEnabled) {
    this.rateLimiterEnabled = rateLimiterEnabled;
    }
    
    public Double getLastUpdated() {
    return lastUpdated;
    }
    
    public void setLastUpdated(Double lastUpdated) {
    this.lastUpdated = lastUpdated;
    }
    
    public Double getCreatedAt() {
    return createdAt;
    }
    
    public void setCreatedAt(Double createdAt) {
    this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
    return "ProjectConfig{" +
    "userId=" + userId +
    ", rateLimiter=" + rate_limiter +
    ", encryption=" + encryption +
    ", baseUrl=" + baseUrl +
    ", encryptionEnabled=" + encryptionEnabled +
    ", rateLimiterEnabled=" + rateLimiterEnabled +
    ", lastUpdated=" + lastUpdated +
    ", createdAt=" + createdAt +
    '}';
    }
}
