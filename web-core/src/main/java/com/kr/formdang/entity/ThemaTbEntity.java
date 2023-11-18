package com.kr.formdang.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "thema_tb", schema = "form_dang", catalog = "")
@Getter
@ToString
public class ThemaTbEntity extends DateEntity{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "tid")
    private long tid;
    @Basic
    @Column(name = "aid")
    private long aid;
    @Basic
    @Column(name = "thema_url")
    private String themaUrl;
    @Basic
    @Column(name = "thema_name")
    private String themaName;
    @Basic
    @Column(name = "size")
    private String size;
    @Basic
    @Column(name = "del_flag")
    private byte delFlag;

}
