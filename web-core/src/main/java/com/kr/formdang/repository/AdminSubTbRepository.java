package com.kr.formdang.repository;

import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.AdminTbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AdminSubTbRepository extends JpaRepository<AdminSubTbEntity, Long> {

    AdminSubTbEntity findByAid(Long aid);

    @Modifying
    @Transactional
    @Query("UPDATE AdminSubTbEntity SET inspectionCnt = inspectionCnt + 1 WHERE aid = :aid")
    int countUpInspectionCnt(Long aid);

    @Modifying
    @Transactional
    @Query("UPDATE AdminSubTbEntity SET quizCnt = quizCnt + 1 WHERE aid = :aid")
    int countUpQuizCnt(Long aid);

}
