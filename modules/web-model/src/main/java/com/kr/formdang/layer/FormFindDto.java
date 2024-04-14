package com.kr.formdang.layer;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class FormFindDto {

    private Integer page;
    private Integer type;
    private Long aid;
    private Integer status;
    private Integer order;

    public FormFindDto(Integer page, Integer type, Long aid, Integer status, Integer order) {
        this.page = page;
        this.type = type;
        this.aid = aid;
        this.status = status;
        this.order = order;
    }

    public Integer getType() {
        final int ALL = 99, SURVEY = 0, QUIZ = 1;
        if (this.type == ALL) {
            return null;
        } else if (this.type == SURVEY) {
//            return FormTypeEnum.SURVEY_TYPE.getCode();
            return 0;
        } else if (this.type == QUIZ) {
//            return FormTypeEnum.QUIZ_TYPE.getCode();
            return 1;
        }
        return null;
    }

    public Integer getOrder() {
        final int RECENT = 0, MANY_ANSWER = 1, LAST = 2;
        if (this.order == RECENT) {
//            return FormOrderEnum.RECENT.getCode();
            return 0;
        } else if (this.order == MANY_ANSWER) {
//            return FormOrderEnum.MANY_RESPONSE.getCode();
            return 1;
        } else if (this.order == LAST) {
//            return FormOrderEnum.LAST.getCode();
            return 2;
        }
        return null;
    }

    public Integer getEndFlag() {
        final int PROGRESS = 0, END = 1, TEMP = 2, DEL = 3;
        if (this.status == PROGRESS) {
//            return FormEndFlagEnum.PROGRESS.getCode();
            return 0;
        } else if (this.status == END) {
//            return FormEndFlagEnum.END.getCode();
            return 1;
        } else if (this.status == TEMP) {
//            return FormEndFlagEnum.PROGRESS.getCode();
            return 0;
        } else if (this.status == DEL) {
            return null;
        }
        return null;
    }

    public Integer getDelFlag() {
        final int PROGRESS = 0, END = 1, TEMP = 2, DEL = 3;
        if (this.status == PROGRESS) {
//            return FormDelFlagEnum.NOT_DEL.getCode();
            return 0;
        } else if (this.status == END) {
//            return FormDelFlagEnum.NOT_DEL.getCode();
            return 0;
        } else if (this.status == TEMP) {
//            return FormDelFlagEnum.NOT_DEL.getCode();
            return 0;
        } else if (this.status == DEL) {
//            return FormDelFlagEnum.DEL.getCode();
            return 1;
        }
        return null;
    }

    public Integer getStatus() {
        final int PROGRESS = 0, END = 1, TEMP = 2, DEL = 3;
        if (this.status == PROGRESS) {
//            return FormStatusEnum.NORMAL_STATUS.getCode();
            return 1;
        } else if (this.status == END) {
            return null;
        } else if (this.status == TEMP) {
//            return FormStatusEnum.TEMP_STATUS.getCode();
            return 0;
        } else if (this.status == DEL) {
            return null;
        }
        return null;
    }

}
