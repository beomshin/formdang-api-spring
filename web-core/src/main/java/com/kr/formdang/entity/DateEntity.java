package com.kr.formdang.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class DateEntity {

    @CreatedDate
    @Column(name = "regDt")
    private Timestamp regDt;

    @LastModifiedDate
    @Column(name = "modDt")
    private Timestamp modDt;

}
