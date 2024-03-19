package com.trysixth.sixth;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.trysixth.sixth.initializers.RLInitializer;
import com.trysixth.sixth.models.Encryption;
import com.trysixth.sixth.models.ProjectConfig;
import com.trysixth.sixth.models.RateLimiter;
import com.trysixth.sixth.utils.CacheUtils;

import org.springframework.context.ApplicationContext;
import java.util.UUID;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;



public class Sixth{
    
    private String apiKey = "";
    private String baseUrl = "";
    private RestTemplate restTemplate;
    private ApplicationContext applicationContext;
    private final List<String> urlList = new ArrayList<>();
    private ProjectConfig projectConfig;
    private CacheUtils cacheUtils;



    public Sixth(String apiKey, SpringApplication app) {
        this.apiKey = apiKey;
        this.baseUrl =  "https://backend.withsix.co";   
        CacheManager cacheManager = new ConcurrentMapCacheManager();
        this.cacheUtils = new CacheUtils(cacheManager);
        this.cacheUtils.saveToCache("apiKey", apiKey);
        app.addInitializers(new RLInitializer(cacheUtils));
    }


    public void extractRoutes() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, org.springframework.web.method.HandlerMethod> map = mapping.getHandlerMethods();
        map.forEach((info, method) -> {
            Set<String> patterns = info.getPatternsCondition() != null ?
            info.getPatternsCondition().getPatterns() :
            info.getDirectPaths();
            urlList.addAll(patterns);
        });
    }

    

    public void init(ApplicationContext app){
        this.applicationContext = app;
        extractRoutes();
        this.cacheUtils.saveToCache("routes", urlList);
        this.restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try{
            String projectConfigUrl = this.baseUrl + "/project-config/config/" + this.apiKey;
            this.projectConfig = restTemplate.getForObject(projectConfigUrl, ProjectConfig.class);          
        }catch(Exception e){
            //no project connfig
        }
        if (this.projectConfig == null) {
            this.projectConfig = this.syncProjectRoute(null);
        } else {
            this.syncProjectRoute(this.projectConfig);
        }
    }



    private ProjectConfig syncProjectRoute(ProjectConfig config) {
        Map<String, RateLimiter> rlConfigs = new HashMap<>();
        for (String route : urlList) {
            
            String editedRoute = route.replaceAll("\\W+", "~");
            if (config != null && config.getRateLimiter().containsKey(editedRoute)) {
                rlConfigs.put(editedRoute, config.getRateLimiter().get(editedRoute));
                continue;
            }
            RateLimiter rlConfig = new RateLimiter();
            String uuid = UUID.randomUUID().toString();
            Map<String, Map<String, String>> errorPayload = new HashMap<>();
            Map<String, String> maxLimitReached = new HashMap<String, String>();
            maxLimitReached.put("message", "max_limit_request_reached");
            maxLimitReached.put("uid", uuid);
            errorPayload.put(uuid, maxLimitReached);
            rlConfig.setId(editedRoute);
            rlConfig.setRoute(editedRoute);
            rlConfig.setInterval(60);
            rlConfig.setRateLimit(10);
            rlConfig.setLastUpdated(getNow());
            rlConfig.setCreatedAt(getNow());
            rlConfig.setUniqueId("host");
            rlConfig.setRateLimitType("ip address");
            rlConfig.setActive(false);
            rlConfigs.put(editedRoute, rlConfig);
            rlConfig.setErrorPayloadId(uuid);
            rlConfig.setErrorPayload(errorPayload);
        }

       
        ProjectConfig projectConfig = new ProjectConfig();
        projectConfig.setUserId(this.apiKey);
        projectConfig.setRateLimiter(rlConfigs);
        projectConfig.setBaseUrl("project");
        projectConfig.setLastUpdated(getNow());
        projectConfig.setCreatedAt(getNow());
        projectConfig.setEncryptionEnabled(false);
        projectConfig.setRateLimiterEnabled(true);
        Encryption encryption = new Encryption();
        encryption.setCreatedAt(getNow());
        encryption.setLastUpdated(getNow());
        encryption.setPrivateKey("dummy");
        encryption.setPublicKey("dummy");
        encryption.setUseCount(0);
        projectConfig.setEncryption(encryption);
       
        ResponseEntity<String> response = restTemplate.postForEntity(
        baseUrl + "/project-config/config/sync-user-config",
        projectConfig, String.class
        );
        return projectConfig;
    }

    private double getNow() {
        return Instant.now().getEpochSecond() + Instant.now().getNano() / 1_000_000_000.0;
    }
}