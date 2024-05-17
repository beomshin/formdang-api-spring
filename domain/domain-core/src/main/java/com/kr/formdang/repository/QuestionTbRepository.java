package com.kr.formdang.repository;

import com.kr.formdang.entity.QuestionTbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionTbRepository extends JpaRepository<QuestionTbEntity, Long> {

    List<QuestionTbEntity> findByFid(Long fid);
    List<QuestionTbEntity> findByFidOrderByOrderAsc(Long fid);

    @Modifying
    @Transactional
    @Query("UPDATE QuestionTbEntity SET imageUrl = :imageUrl WHERE fid = :fid AND order = :order")
    void updateImageUrl(Long fid, String imageUrl, Integer order);
}
