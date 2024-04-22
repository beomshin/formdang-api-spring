package com.kr.formdang.repository;

import com.kr.formdang.entity.AuthTbEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<AuthTbEntity, Long> {

    long countBySecret(String secret);
}
