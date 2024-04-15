package com.kr.formdang.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
@Slf4j
public class JasyptConfig {

    @Value("${encryptor.key}")
    private String password; // jvm option 을 통해 전달

    private final String ALGORITHM = "PBEWITHHMACSHA512ANDAES_256"; // HmacSHA512로 부터 생성된 키를 사용하여 데이터를 AES-256 알고리즘을 이용하여 암호화
    private final int SALT = 3308;
    private final int POOL_SIZE = 1;
    private final String ENCODING = "base64";

    /**
     * properties 중요 정보 암,복호화 모듈
     * @return
     */
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(){

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(password); // 암호화 키
        config.setAlgorithm(ALGORITHM); // 적용 알고리즘
        config.setKeyObtentionIterations(SALT); // 해싱 반복 횟수
        config.setPoolSize(POOL_SIZE); // 인스턴스 Pool 개수
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator"); // IV 생성기
        config.setStringOutputType(ENCODING); // 인코딩 방식
        encryptor.setConfig(config);

        return encryptor;
    }
}
