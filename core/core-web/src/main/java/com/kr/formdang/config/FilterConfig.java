package com.kr.formdang.config;

import com.kr.formdang.filters.DefaultHttpLoggingFilter;
import com.kr.formdang.filters.DefaultMDCLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class FilterConfig {


    @Bean
    public FilterRegistrationBean DefaultMDCLoggingFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new DefaultMDCLoggingFilter());
        filterFilterRegistrationBean.setOrder(Integer.MIN_VALUE);
        filterFilterRegistrationBean.addUrlPatterns("/*");
        return filterFilterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean DefaultHttpLoggingFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new DefaultHttpLoggingFilter());
        filterFilterRegistrationBean.setOrder(Integer.MIN_VALUE + 1);
        filterFilterRegistrationBean.addUrlPatterns("/*");
        return filterFilterRegistrationBean;
    }

}
