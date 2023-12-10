package com.kr.formdang.config.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final static String commonRestTemplate = "commonRestTemplate";

    @Bean(name = RestTemplateConfig.commonRestTemplate)
    public RestTemplate restTemplate() {

        // 1. 타임아웃 설정시 HttpComponentsClientHttpRequestFactory 객체를 생성합니다.
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 타임아웃 설정 5초
        factory.setReadTimeout(15000); // 타임아웃 설정 15초

        //Apache HttpComponents : 각 호스트(IP와 Port의 조합)당 커넥션 풀에 생성가능한 커넥션 수
        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(50)//최대 커넥션 수
                .setMaxConnPerRoute(20).build();

        factory.setHttpClient(httpClient);

        return new RestTemplate(factory);
    }

}
