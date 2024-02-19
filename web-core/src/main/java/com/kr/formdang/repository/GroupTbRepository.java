package com.kr.formdang.repository;

import com.kr.formdang.entity.GroupFormTbEntity;
import com.kr.formdang.entity.GroupTbEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupTbRepository extends JpaRepository<GroupTbEntity, Long> {

}
