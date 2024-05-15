package com.kr.formdang.config;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;


@Configuration
public class RestTemplateConfig {

    /**
     * 내부 인증서버 REST 템플릿 싱글톤 생성
     * @return
     */
    @Bean(name = "commonRestTemplate")
    public RestTemplate restTemplate() {
        ClientHttpRequestFactory factory = getHttpRequestFactory(5, 10, 50, 50);
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Collections.singletonList(clientHttpRequestInterceptor()));
        return restTemplate;
    }

    /**
     * SNS 로그인 REST 템플릿 싱글톤 생성
     * @return
     */
    @Bean(name = "snsApiRestTemplate")
    public RestTemplate restTemplate2() {
        ClientHttpRequestFactory factory = getHttpRequestFactory(5, 15, 50, 25);
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Collections.singletonList(clientHttpRequestInterceptor()));
        return restTemplate;
    }

    /**
     * connection 타임아웃, read 타임아웃, max 커넥션, route per max 커넥션 팩토리 생성
     * @param connectTimeout
     * @param readTimeout
     * @param maxConn
     * @param maxConnPerRoute
     * @return
     */
    private ClientHttpRequestFactory getHttpRequestFactory(int connectTimeout, int readTimeout, int maxConn, int maxConnPerRoute) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(connectTimeout).getNano()); // 타임아웃 설정 5초
        factory.setReadTimeout(Duration.ofSeconds(readTimeout).getNano()); // 타임아웃 설정 10초

        //Apache HttpComponents : 각 호스트(IP와 Port의 조합)당 커넥션 풀에 생성가능한 커넥션 수
        factory.setHttpClient(HttpClientBuilder.create()
                .setMaxConnTotal(maxConn)//최대 커넥션 수
                .setMaxConnPerRoute(maxConnPerRoute) //IP, 포트 1쌍에 대해 수행할 커넥션 수, 특정 경로당 최대 숫자
                .build());

        return factory;
    }

    /**
     * REST 템플릿 retry 인터셉터 등록
     * @return
     */
    private ClientHttpRequestInterceptor clientHttpRequestInterceptor() {
        return (request, body, execution) -> {
            RetryTemplate retryTemplate = new RetryTemplate();
            retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3));
            try {
                return retryTemplate.execute(context -> execution.execute(request, body));
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };
    }
}
