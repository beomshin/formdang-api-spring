package com.kr.formdang.repository;

import com.kr.formdang.entity.AnswerTbEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerTbRepository extends JpaRepository<AnswerTbEntity, Long> {

    int countByFidAndAid(long fid, long aid);

    List<AnswerTbEntity> findByFidAndAid(long fid, long aid);
}
