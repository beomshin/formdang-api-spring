package com.kr.formdang.entity;

import com.kr.formdang.model.layer.FormDataDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "form_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class FormTbEntity extends DateEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "fid")
    private Long fid;
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
    private String themeUrl;
    @Basic
    @Column(name = "question_count")
    private int questionCount;
    @Basic
    @Column(name = "report_count")
    private int reportCount;
    @Basic
    @Column(name = "answer_count")
    private int answerCount;
    @Basic
    @Column(name = "status")
    private int status;
    @Basic
    @Column(name = "end_flag")
    private int endFlag;
    @Basic
    @Column(name = "del_flag")
    private int delFlag;

    public void updateForm(FormDataDto formDataDto) {
        this.formType = formDataDto.getType();
        this.title = formDataDto.getTitle();
        this.formDetail = formDataDto.getDetail();
        this.beginDt = formDataDto.getBeginDt();
        this.endDt = formDataDto.getEndDt();
        this.questionCount = formDataDto.getQuestionCount();
        this.status = formDataDto.getStatus();
        this.maxRespondent = formDataDto.getMaxRespondent();
        this.logoUrl = formDataDto.getLogoUrl();
        this.themeUrl = formDataDto.getThemeUrl();
        super.updateModDt();
    }
}
