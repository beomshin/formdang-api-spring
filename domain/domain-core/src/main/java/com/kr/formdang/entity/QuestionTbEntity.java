package com.kr.formdang.entity;

import com.kr.formdang.layer.QuestionDataDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Arrays;
import java.util.stream.Collectors;

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

    public void updateQuestion(QuestionDataDto questionDataDto) {
        this.order = questionDataDto.getOrder();
        this.questionType = questionDataDto.getType();
        this.title = questionDataDto.getTitle();
        this.questionPlaceholder = questionDataDto.getPlaceholder();
        this.questionDetail = questionDataDto.getDetail() != null ?
                Arrays.stream(questionDataDto.getDetail()).collect(Collectors.joining("|")) : null;
        this.questionExampleDetail = questionDataDto.getExampleDetail() != null ?
                Arrays.stream(questionDataDto.getExampleDetail()).collect(Collectors.joining("|")) : null;
        this.count = questionDataDto.getCount();
        this.quizAnswer = questionDataDto.getAnswer() != null ?
                Arrays.stream(questionDataDto.getAnswer()).collect(Collectors.joining("|")) : null;
        this.imageUrl = questionDataDto.getImageUrl();
        super.updateModDt();
    }
}
