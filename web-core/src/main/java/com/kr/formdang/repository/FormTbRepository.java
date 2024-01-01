package com.kr.formdang.repository;

import com.kr.formdang.entity.FormTbEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormTbRepository extends JpaRepository<FormTbEntity, Long> {

    Page<FormTbEntity> findByAidOrderByRegDtDesc(Long aid, Pageable pageable);
    Page<FormTbEntity> findByFormTypeAndAidOrderByRegDtDesc(Integer formType, Long aid, Pageable pageable);
    Page<FormTbEntity> findByAidAndStatusOrderByRegDtDesc(Long aid, Integer status , Pageable pageable);
    Page<FormTbEntity> findByAidAndEndFlagOrderByRegDtDesc(Long aid, Integer endFlag , Pageable pageable);
}
