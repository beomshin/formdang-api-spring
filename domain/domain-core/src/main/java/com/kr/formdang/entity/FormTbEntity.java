package com.kr.formdang.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
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
    @Basic
    @Column(name = "login_flag")
    private int loginFlag;

    public boolean isUserSubmitForm() { // 작성자가 있는 경우 시작 폼
        return this.answerCount > 0;
    }

    public boolean isDeleteForm() { // 삭제 플래그(1) 경우 삭제
        return this.delFlag == 1;
    }

    public boolean isEndForm() { // 설문 종료 플래그(1) 경우 종료
        return this.endFlag == 1;
    }

    public boolean isStartForm() {
        return this.status == 1;
    }

    public boolean isExceedSubject() {
        return maxRespondent != 0 && answerCount > maxRespondent;
    }

    public boolean isQuiz() {
        return this.formType == 1;
    }

    public boolean isSubmitRangeDt() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return now.compareTo(beginDt) >= 0 && now.compareTo(endDt) <= 0;
    }

    public void modify(FormTbEntity modifyData) {
        super.modDt = new Timestamp(System.currentTimeMillis());
        this.formType = modifyData.getFormType();
        this.title = modifyData.getTitle();
        this.formDetail = modifyData.getFormDetail();
        this.beginDt = modifyData.getBeginDt();
        this.endDt = modifyData.getEndDt();
        this.questionCount = modifyData.getQuestionCount();
        this.status = modifyData.getStatus();
        this.maxRespondent = modifyData.getMaxRespondent();
        this.logoUrl = modifyData.getLogoUrl();
        this.themeUrl = modifyData.getThemeUrl();
        this.loginFlag = modifyData.getLoginFlag();
    }
}
