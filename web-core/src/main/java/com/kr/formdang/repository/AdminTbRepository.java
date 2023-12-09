package com.kr.formdang.repository;

import com.kr.formdang.entity.AdminTbEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminTbRepository extends JpaRepository<AdminTbEntity, Long> {

    Optional<AdminTbEntity> findBySubId(String sub_id);
}
