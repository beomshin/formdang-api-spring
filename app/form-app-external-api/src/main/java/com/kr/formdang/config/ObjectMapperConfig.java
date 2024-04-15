package com.kr.formdang.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ObjectMapperConfig {

    private final static String API_OBJECT_MAPPER = "apiObjectMapper";

    @Bean(name = ObjectMapperConfig.API_OBJECT_MAPPER)
    public ObjectMapper objectMapper() {
        log.info("[Bean][apiObjectMapper] 설정 ");
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // unknown property 처리 방식 설정 ( false 를 통해 무시 처리)
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE); // 카카오, 구글 응답 값 SNAKE CASE로 해당 전략 사용
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
        return objectMapper;
    }

}
