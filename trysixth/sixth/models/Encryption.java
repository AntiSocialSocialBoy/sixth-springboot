package com.trysixth.sixth.models;

import java.util.Map;
import java.util.UUID;
import java.util.Map;
import java.util.Optional;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Encryption {
    @JsonProperty("public_key")
    private String publicKey;
    @JsonProperty("private_key")
    private String privateKey;
    @JsonProperty("use_count")
    private int useCount;
    @JsonProperty("last_updated")
    private double lastUpdated;
    @JsonProperty("created_at")
    private double createdAt;

    // Constructors
    public Encryption() {
    }

    public Encryption(String publicKey, String privateKey, int useCount, double lastUpdated, double createdAt) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.useCount = useCount;
        this.lastUpdated = lastUpdated;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
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

    @Override
    public String toString() {
        return "ProjectConfig{" +
        "publicKey=" + publicKey +
        ", privateKey=" + privateKey +
        ", useCount=" + useCount +
        ", lastUpdated=" + lastUpdated +
        ", createdAt=" + createdAt;
    }
}
