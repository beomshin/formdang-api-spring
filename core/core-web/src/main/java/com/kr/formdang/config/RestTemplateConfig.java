package com.kr.formdang.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Arrays;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final static String COMMON_REST_TEMPLATE = "commonRestTemplate";

    private final static String SNS_API_REST_TEMPLATE = "snsApiRestTemplate";

    /**
     * 폼당 인증서버 요청 템플릿
     * @return
     */
    @Bean(name = RestTemplateConfig.COMMON_REST_TEMPLATE)
    public RestTemplate restTemplate() {
        log.info("[Bean][commonRestTemplate] 설정 ");

        // 1. 타임아웃 설정시 HttpComponentsClientHttpRequestFactory 객체를 생성합니다.
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5).getNano()); // 타임아웃 설정 5초
        factory.setReadTimeout(Duration.ofSeconds(10).getNano()); // 타임아웃 설정 10초

        //Apache HttpComponents : 각 호스트(IP와 Port의 조합)당 커넥션 풀에 생성가능한 커넥션 수
        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(50)//최대 커넥션 수
                .setMaxConnPerRoute(50) //IP, 포트 1쌍에 대해 수행할 커넥션 수, 특정 경로당 최대 숫자
                .build();

        factory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Arrays.asList(clientHttpRequestInterceptor()));

        return new RestTemplate(factory);
    }

    /**
     * sns 로그인 템플릿
     * @return
     */
    @Bean(name = RestTemplateConfig.SNS_API_REST_TEMPLATE)
    public RestTemplate restTemplate2() {
        log.info("[Bean][snsApiRestTemplate] 설정 ");

        // 1. 타임아웃 설정시 HttpComponentsClientHttpRequestFactory 객체를 생성합니다.
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5).getNano()); // 타임아웃 설정 5초
        factory.setReadTimeout(Duration.ofSeconds(15).getNano()); // 타임아웃 설정 15초

        //Apache HttpComponents : 각 호스트(IP와 Port의 조합)당 커넥션 풀에 생성가능한 커넥션 수
        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(50)//최대 커넥션 수
                .setMaxConnPerRoute(25) //IP, 포트 1쌍에 대해 수행할 커넥션 수, 특정 경로당 최대 숫자
                .build();

        factory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Arrays.asList(clientHttpRequestInterceptor()));

        return new RestTemplate(factory);
    }

    /**
     * 최대 3번 Retry
     * @return
     */
    public ClientHttpRequestInterceptor clientHttpRequestInterceptor() {
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
