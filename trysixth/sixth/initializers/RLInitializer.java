package com.trysixth.sixth.initializers;
import java.util.EnumSet;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.trysixth.sixth.middlewares.SixthRateLimiterMiddleware;
import com.trysixth.sixth.utils.CacheUtils;

public class RLInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>  {
    CacheUtils cacheUtils;
    public RLInitializer(CacheUtils cacheUtils){
        this.cacheUtils = cacheUtils;
    }   
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        SixthRateLimiterMiddleware myFilter = new SixthRateLimiterMiddleware(cacheUtils); // Replace MyFilter with your filter class
        
        FilterRegistrationBean<SixthRateLimiterMiddleware> registration = new FilterRegistrationBean<>();
        registration.setFilter(myFilter);
        registration.addUrlPatterns("/*"); // Apply to all URLs
        registration.setOrder(0); // Set filter order if needed
        
        applicationContext.addBeanFactoryPostProcessor(beanFactory -> {
            beanFactory.registerSingleton("SixthRateLimiterMiddleWare", registration);
        });
        
       
    }
    
}
