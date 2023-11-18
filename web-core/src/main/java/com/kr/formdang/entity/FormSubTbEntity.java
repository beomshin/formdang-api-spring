package com.kr.formdang.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "form_sub_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class FormSubTbEntity extends DateEntity{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "fsid")
    private long fsid;
    @Basic
    @Column(name = "fid")
    private long fid;
    @Basic
    @Column(name = "form_url")
    private String formUrl;
    @Basic
    @Column(name = "form_qr")
    private String formQr;
    @Basic
    @Column(name = "form_web_hock_url")
    private String formWebHockUrl;

}
