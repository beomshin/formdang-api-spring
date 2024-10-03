package com.kr.formdang.config;

import com.kr.formdang.filters.DefaultHttpLoggingFilter;
import com.kr.formdang.filters.DefaultMDCLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class FilterConfig {

    /**
     * MDC request-id logging filter 1순위
     * @return
     */
    @Bean
    public FilterRegistrationBean DefaultMDCLoggingFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new DefaultMDCLoggingFilter());
        filterFilterRegistrationBean.setOrder(Integer.MIN_VALUE);
        filterFilterRegistrationBean.addUrlPatterns("/*");
        return filterFilterRegistrationBean;
    }

    /**
     * http request 정보 logging 2순위
     * @return
     */
//    @Bean
//    public FilterRegistrationBean DefaultHttpLoggingFilter() {
//        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
//        filterFilterRegistrationBean.setFilter(new DefaultHttpLoggingFilter());
//        filterFilterRegistrationBean.setOrder(Integer.MIN_VALUE + 1);
//        filterFilterRegistrationBean.addUrlPatterns("/*");
//        return filterFilterRegistrationBean;
//    }

}
