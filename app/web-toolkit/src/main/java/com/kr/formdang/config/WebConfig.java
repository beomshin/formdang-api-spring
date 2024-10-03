package com.kr.formdang.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.AbstractXmlHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        WebMvcConfigurer.super.addFormatters(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // XML 관련 HttpMessageConverter 의 우선순위를 최하위로 낮추는 메소드
        reorderXmlConvertersToEnd(converters);
    }

    private void reorderXmlConvertersToEnd(List<HttpMessageConverter<?>> converters) {
        List<HttpMessageConverter<?>> xml = new ArrayList<>();
        for (Iterator<HttpMessageConverter<?>> iterator =
             converters.iterator(); iterator.hasNext();) {
            HttpMessageConverter<?> converter = iterator.next();
            if ((converter instanceof AbstractXmlHttpMessageConverter)
                    || (converter instanceof MappingJackson2XmlHttpMessageConverter)) {
                xml.add(converter);
                iterator.remove();
            }
        }
        converters.addAll(xml);
    }
}
