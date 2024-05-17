package com.kr.formdang.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "answer_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerTbEntity extends DateEntity{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "awid")
    private Long awid;
    @Basic
    @Column(name = "sid")
    private Long sid;
    @Basic
    @Column(name = "fid")
    private Long fid;
    @Basic
    @Column(name = "qid")
    private Long qid;
    @Basic
    @Column(name = "aid")
    private Long aid;
    @Basic
    @Column(name = "question_type")
    private Byte questionType;
    @Basic
    @Column(name = "s_answer")
    private String sAnswer;
    @Basic
    @Column(name = "m_answer")
    private String mAnswer;
    @Basic
    @Column(name = "ok_flag")
    private int okFlag;

}
