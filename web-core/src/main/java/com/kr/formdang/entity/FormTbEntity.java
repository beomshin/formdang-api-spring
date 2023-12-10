package com.kr.formdang.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "form_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
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
    private int formType;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "form_detail")
    private String formDetail;
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
    @Column(name = "status")
    private int status;
    @Basic
    @Column(name = "end_flag")
    private int endFlag;
    @Basic
    @Column(name = "del_flag")
    private int delFlag;

}
