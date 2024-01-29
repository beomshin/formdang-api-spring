package com.kr.formdang.repository;

import com.kr.formdang.entity.FormTbEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormTbRepository extends JpaRepository<FormTbEntity, Long> {

}
