package com.kr.formdang.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "config_tb", schema = "form_dang", catalog = "")
@Getter
@ToString
public class ConfigTbEntity extends DateEntity{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "cid")
    private long cid;
    @Basic
    @Column(name = "key")
    private String key;
    @Basic
    @Column(name = "value")
    private String value;

}
