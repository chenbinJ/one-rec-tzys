package com.ztgeo.general.configuration;

import com.ztgeo.general.interceptor.AuthPowerCheckInterceptor;
import com.ztgeo.general.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Value("${url.ignore}")
    private String ignore;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/**").excludePathPatterns(ignore.split(","));
        registry.addInterceptor(authPowerCheckInterceptor()).addPathPatterns("/**").excludePathPatterns(ignore.split(","));
        super.addInterceptors(registry);
    }


    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /*
    * 拦截器的装配
    * */
    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Bean
    public AuthPowerCheckInterceptor authPowerCheckInterceptor() {return new AuthPowerCheckInterceptor();}

}
