package com.kr.formdang.repository;

import com.kr.formdang.entity.QuestionTbEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionTbRepository extends JpaRepository<QuestionTbEntity, Long> {

    List<QuestionTbEntity> findByFid(Long fid);
}
