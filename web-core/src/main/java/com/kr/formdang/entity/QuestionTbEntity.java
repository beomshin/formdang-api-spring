package com.kr.formdang.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "question_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class QuestionTbEntity extends DateEntity{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "qid")
    private long qid;
    @Basic
    @Column(name = "fid")
    private Long fid;
    @Basic
    @Column(name = "order")
    private int order;
    @Basic
    @Column(name = "question_type")
    private int questionType;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "question_placeholder")
    private String questionPlaceholder;
    @Basic
    @Column(name = "question_detail")
    private String questionDetail;
    @Basic
    @Column(name = "quiz_answer")
    private String quizAnswer;
    @Basic
    @Column(name = "count")
    private int count;
    @Basic
    @Column(name = "image_url")
    private String imageUrl;

}
