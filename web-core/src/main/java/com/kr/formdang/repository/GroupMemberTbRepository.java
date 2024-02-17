package com.kr.formdang.repository;

import com.kr.formdang.entity.GroupFormTbEntity;
import com.kr.formdang.entity.GroupMemberTbEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupMemberTbRepository extends JpaRepository<GroupMemberTbEntity, Long> {

    Optional<GroupMemberTbEntity> findByAidAndGid(Long aid, Long gid);
}
