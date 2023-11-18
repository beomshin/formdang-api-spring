package com.kr.formdang.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "form_tb", schema = "form_dang", catalog = "")
@Getter
@ToString
public class FormTbEntity extends DateEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "fid")
    private long fid;
    @Basic
    @Column(name = "aid")
    private Long aid;
    @Basic
    @Column(name = "form_type")
    private byte formType;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "begin_dt")
    private Timestamp beginDt;
    @Basic
    @Column(name = "end_dt")
    private Timestamp endDt;
    @Basic
    @Column(name = "max_respondent")
    private int maxRespondent;
    @Basic
    @Column(name = "logo_url")
    private String logoUrl;
    @Basic
    @Column(name = "thema_url")
    private String themaUrl;
    @Basic
    @Column(name = "question_count")
    private int questionCount;
    @Basic
    @Column(name = "report_count")
    private int reportCount;
    @Basic
    @Column(name = "end_flag")
    private byte endFlag;
    @Basic
    @Column(name = "del_flag")
    private byte delFlag;

}
