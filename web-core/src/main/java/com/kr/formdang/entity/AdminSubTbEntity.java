package com.kr.formdang.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "admin_sub_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class AdminSubTbEntity extends DateEntity{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "asid")
    private long asid;
    @Basic
    @Column(name = "aid")
    private long aid;
    @Basic
    @Column(name = "inspection_cnt")
    private Integer inspectionCnt;
    @Basic
    @Column(name = "quiz_cnt")
    private Integer quizCnt;
    @Basic
    @Column(name = "inspection_respondent_cnt")
    private Integer inspectionRespondentCnt;
    @Basic
    @Column(name = "quiz_respondent_cnt")
    private Integer quizRespondent_cnt;
}
