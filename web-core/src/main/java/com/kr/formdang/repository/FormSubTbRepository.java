package com.kr.formdang.repository;

import com.kr.formdang.entity.FormSubTbEntity;
import com.kr.formdang.entity.FormTbEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormSubTbRepository extends JpaRepository<FormSubTbEntity, Long> {

    Optional<FormSubTbEntity> findByFormTbAndFormUrlKey(FormTbEntity formTbEntity, String key);
}
