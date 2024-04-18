package com.kr.formdang.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "question_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class QuestionTbEntity extends DateEntity{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "qid")
    private long qid;
    @Basic
    @Column(name = "fid")
    @Setter
    private Long fid;
    @Basic
    @Column(name = "`order`")
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
    @Column(name = "question_example_detail")
    private String questionExampleDetail;
    @Basic
    @Column(name = "quiz_answer")
    private String quizAnswer;
    @Basic
    @Column(name = "count")
    private Integer count;
    @Basic
    @Column(name = "image_url")
    private String imageUrl;

    public void updateQuestion(QuestionTbEntity questionDataDto) {
        this.order = questionDataDto.getOrder();
        this.questionType = questionDataDto.getQuestionType();
        this.title = questionDataDto.getTitle();
        this.questionPlaceholder = questionDataDto.getQuestionPlaceholder();
        this.questionDetail = questionDataDto.getQuestionDetail();
        this.questionExampleDetail = questionDataDto.getQuestionExampleDetail();
        this.count = questionDataDto.getCount();
        this.quizAnswer = questionDataDto.getQuizAnswer();
        this.imageUrl = questionDataDto.getImageUrl();
        super.updateModDt();
    }
}
