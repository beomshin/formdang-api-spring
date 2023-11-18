package com.kr.formdang.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "logo_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class LogoTbEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "lid")
    private long lid;
    @Basic
    @Column(name = "aid")
    private long aid;
    @Basic
    @Column(name = "logo_url")
    private String logoUrl;
    @Basic
    @Column(name = "logo_name")
    private String logoName;
    @Basic
    @Column(name = "size")
    private String size;
    @Basic
    @Column(name = "del_flag")
    private byte delFlag;
    @Basic
    @Column(name = "reg_dt")
    private Timestamp regDt;
    @Basic
    @Column(name = "mod_dt")
    private Timestamp modDt;

}
