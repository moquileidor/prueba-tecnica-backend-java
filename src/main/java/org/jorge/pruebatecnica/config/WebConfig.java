package org.jorge.pruebatecnica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
        
        registry.addResourceHandler("/*.html")
                .addResourceLocations("classpath:/static/");
        
        registry.addResourceHandler("/*.css")
                .addResourceLocations("classpath:/static/");
        
        registry.addResourceHandler("/*.js")
                .addResourceLocations("classpath:/static/");
    }
}
