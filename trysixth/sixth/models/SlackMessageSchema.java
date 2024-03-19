package com.trysixth.sixth.models;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


public class SlackMessageSchema {
    @JsonProperty("header")
    private JSONObject header;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("body")
    private JSONObject body;
    @JsonProperty("query_args")
    private String queryParams;
    @JsonProperty("timestamp")
    private double timestamp;
    @JsonProperty("attack_type")
    private String attackType;
    @JsonProperty("cwe_link")
    private String cweLink;
    @JsonProperty("status")
    private String status;
    @JsonProperty("learn_more_link")
    private String learnMoreLink;
    @JsonProperty("route")
    private String route;

    public JSONObject getHeader() {
        return header;
    }

    public void setHeader(JSONObject header) {
        this.header = header;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }

    public Object getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public String getAttackType() {
        return attackType;
    }

    public void setAttackType(String attackType) {
        this.attackType = attackType;
    }

    public String getCweLink() {
        return cweLink;
    }

    public void setCweLink(String cweLink) {
        this.cweLink = cweLink;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLearnMoreLink() {
        return learnMoreLink;
    }

    public void setLearnMoreLink(String learnMoreLink) {
        this.learnMoreLink = learnMoreLink;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}