package com.kr.formdang.repository;

import com.kr.formdang.entity.FormTbEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface FormTbRepository extends JpaRepository<FormTbEntity, Long> {

    Optional<FormTbEntity> findByAidAndFid(Long aid, Long fid);

    @Modifying
    @Transactional
    @Query("UPDATE FormTbEntity SET logoUrl = :logoUrl WHERE fid = :fid")
    int updateLogoUrl(Long fid, String logoUrl);
}
