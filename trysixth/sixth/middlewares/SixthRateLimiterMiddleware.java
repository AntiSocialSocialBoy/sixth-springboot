package com.trysixth.sixth.middlewares;
import org.json.JSONObject;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trysixth.sixth.models.ProjectConfig;
import com.trysixth.sixth.models.RLRequestBody;
import com.trysixth.sixth.models.RLResponse;
import com.trysixth.sixth.models.RateLimiter;
import com.trysixth.sixth.models.SlackMessageSchema;
import com.trysixth.sixth.utils.CacheUtils;
import com.trysixth.sixth.utils.CachedBodyHttpServletRequest;
import com.trysixth.sixth.utils.MultiReadHttpServletRequest;

import javax.servlet.annotation.WebFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebFilter("/*")
public class SixthRateLimiterMiddleware implements Filter {
    private String apikey;
    private ProjectConfig config;
    private List<String> routes;
    private HashMap<String, Double>  routeLastUpdated = new HashMap<>();
    private HashMap<String, Double>  rateLimitLogsSent = new HashMap<>();
    private CacheUtils cacheUtils;
    

    public SixthRateLimiterMiddleware(CacheUtils cacheUtils){
        this.cacheUtils = cacheUtils;
        this.apikey = (String) cacheUtils.getFromCache("apiKey");
        // this.routes = routes;
        // this.config = config;
        // for (int i=0; i <=routes.size()-1; i++){
        //     String editedRoute = routes.get(i).replace("/", "~");
        //     this.routeLastUpdated.put(editedRoute, getNow());
        //     this.rateLimitLogsSent.put(editedRoute, 0.0);
        // }
    }

    private boolean isRateLimitReached(String uid, String route, RateLimiter rl){
        int rateLimit = rl.getRateLimit();
        double interval = rl.getInterval();
        RLRequestBody body = new RLRequestBody();
        body.setRoute(route);
        body.setInterval(interval);
        body.setRateLimit(rateLimit);
        body.setUniqueId(uid.replace(".", "~"));
        body.setUserId(this.apikey);
        body.setActive(true);
        RestTemplate rst = new RestTemplate();
        RLResponse resp = rst.postForObject(
            "https://backend.withsix.co/rate-limit/enquire-has-reached-rate_limit", 
            body,
            RLResponse.class);
        if (resp.getResponse()){
            return resp.getResponse();
        }
        
        return false;  
    }

    private void sendLogs(String route, JSONObject body, JSONObject header, String query){
        double timeStamp = getNow();
        double lastUpdated = this.rateLimitLogsSent.get(route);
        if (timeStamp - lastUpdated > 10){
            RestTemplate rst = new RestTemplate();
            SlackMessageSchema rstBody = new SlackMessageSchema();
            rstBody.setHeader(header);
            rstBody.setUserId(this.apikey);
            rstBody.setBody(body);
            rstBody.setQueryParams(query);
            rstBody.setTimestamp(timeStamp);
            rstBody.setAttackType("No Rate Limit Attack");
            rstBody.setCweLink("https://cwe.mitre.org/data/definitions/770.html");
            rstBody.setStatus("MITIGATED");
            rstBody.setLearnMoreLink("https://en.wikipedia.org/wiki/Rate_limiting");
            rstBody.setRoute(route);
            rst.postForObject("https://backend.withsix.co/slack/send_message_to_slack_user", rstBody, String.class);
        }
    }
    
    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response,
            jakarta.servlet.FilterChain chain) throws IOException, jakarta.servlet.ServletException {
        if (this.routes == null){
            this.routes = (List<String>) this.cacheUtils.getFromCache("routes");
            for (int i=0; i <=this.routes.size()-1; i++){
                String editedRoute = routes.get(i).replace("/", "~");
                this.routeLastUpdated.put(editedRoute, getNow());
                this.rateLimitLogsSent.put(editedRoute, 0.0);
            }
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        CachedBodyHttpServletRequest multiReadRequest = new CachedBodyHttpServletRequest(httpRequest);
        
           
        String host = request.getRemoteHost();
        String route = httpRequest.getRequestURI();
        route = route.replace("/", "~");
        Enumeration<String> headerEnum = httpRequest.getHeaderNames();
        JSONObject headers = new JSONObject();
        Map<String, String[]> queryParamsEnum = httpRequest.getParameterMap();
        JSONObject queryParams = new JSONObject();

        while (headerEnum.hasMoreElements()) {
            String key = headerEnum.nextElement();
            String value = httpRequest.getHeader(key);
            headers.put(key, value);
        }
       
        for (Map.Entry<String, String[]> entry : queryParamsEnum.entrySet()) {
            String paramName = entry.getKey();
            String[] paramValues = entry.getValue();
            for (String paramValue : paramValues) {
                queryParams.put(paramName, paramValue);
            }
        }
        RateLimiter rl = new RateLimiter();
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = multiReadRequest.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        JSONObject body = new JSONObject();
        try{
            body = new JSONObject(requestBody.toString());
        }catch(Exception e){

        }
     
        try{
            RestTemplate rst = new RestTemplate();
            double updateTime = getNow();
            rl = rst.getForObject("https://backend.withsix.co/project-config/config/get-route-rate-limit/"+this.apikey+"/"+route,  RateLimiter.class);
            this.routeLastUpdated.replace(route, updateTime);
            try{
                if (rl.isActive()){
                    String preferredId = rl.getUniqueId();
                    if (preferredId == "" || preferredId=="host"){
                        preferredId = host;
                    }else{
                        
                        if (rl.getRateLimitType().equals("body")){
                            preferredId = body.getString(preferredId);
                        }
                        else if (rl.getRateLimitType().equals("header")){
                            preferredId = headers.getString(preferredId);
                        }
                        else if (rl.getRateLimitType().equals("args")){
                            preferredId = queryParams.getString(preferredId);
                        }
                        else{
                            
                            preferredId = host;
                        }
                    }
                    JSONObject rulesObject = new JSONObject();
                    rulesObject.put("default", preferredId);
                    if (rl.getRateLimitByRules() != null){
                        for (Entry<String, String> entry : rl.getRateLimitByRules().entrySet()) {
                            String key = entry.getKey();
                            Object valueObj = entry.getValue();
                            JSONObject value = new JSONObject(valueObj);
                            if (key == "body"){
                                if (body != null){
                                    rulesObject.put("body",   body.get((String) rl.getRateLimitByRules().get("body")));
                                }
                            }else if (key == "header"){
                                rulesObject.put("headers",   headers.get((String) rl.getRateLimitByRules().get("headers")));
                            }
                            else if (key == "args"){
                                rulesObject.put("args",   queryParams.get((String) rl.getRateLimitByRules().get("args")));
                            }
                            
                        }
                    }
                    Iterator<String> keys = rulesObject.keys();
                    String finalRule = "";
                    while (keys.hasNext()) {
                        String keyx = keys.next();
                        String valuex = rulesObject.getString(keyx);
                        finalRule += valuex;
                    }
                    if (isRateLimitReached(finalRule, route, rl)){
                        ObjectMapper objectMapper = new ObjectMapper();
                        String bodyJson = objectMapper.writeValueAsString(body);
                        String headerJson = objectMapper.writeValueAsString(headers);
                        sendLogs(route, body,headers, queryParams.toString());
                        HttpServletResponse httpResponse = (HttpServletResponse) response;
                        httpResponse.setStatus(420);
                        // Optionally, you can set a custom error message
                        httpResponse.setContentType("application/json");
                        httpResponse.setCharacterEncoding("UTF-8");
                        httpResponse.getWriter().write(String.format("{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"path\":\"%s\"}",
            new Date(), 420, "Max limit reached", httpRequest.getRequestURI()));
                       
                        // End the filter chain
                        return;
                    }else{
                        chain.doFilter(multiReadRequest, response);
                    }
                }else{
                    chain.doFilter(multiReadRequest, response);
                }
            }catch(Exception e){
                e.printStackTrace();
                chain.doFilter(multiReadRequest, response);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

       
        chain.doFilter(multiReadRequest, response);
    }


    private double getNow() {
        return Instant.now().getEpochSecond() + Instant.now().getNano() / 1_000_000_000.0;
    }


    
}
