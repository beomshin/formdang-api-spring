package com.kr.formdang.repository;

import com.kr.formdang.entity.GroupFormTbEntity;
import com.kr.formdang.entity.GroupMemberTbEntity;
import com.kr.formdang.entity.GroupTbEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberTbRepository extends JpaRepository<GroupMemberTbEntity, Long> {

    List<GroupMemberTbEntity> findByAidAndGroupTbIn(Long aid, List<GroupTbEntity> gid);
}
