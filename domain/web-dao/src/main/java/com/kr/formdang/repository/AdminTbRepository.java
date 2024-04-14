package com.kr.formdang.repository;

import com.kr.formdang.entity.AdminTbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AdminTbRepository extends JpaRepository<AdminTbEntity, Long> {

    Optional<AdminTbEntity> findBySubId(String sub_id);

    @Modifying
    @Transactional
    @Query("UPDATE AdminTbEntity SET profile = :profile WHERE aid = :aid")
    int updateProfile(Long aid, String profile);
}
