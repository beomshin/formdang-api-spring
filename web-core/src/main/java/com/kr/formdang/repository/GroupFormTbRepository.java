package com.kr.formdang.repository;

import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.GroupFormTbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface GroupFormTbRepository extends JpaRepository<GroupFormTbEntity, Long> {

    Optional<GroupFormTbEntity> findByFid(Long fid);
}
