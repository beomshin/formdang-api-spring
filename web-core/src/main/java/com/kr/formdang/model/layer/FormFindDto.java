package com.kr.formdang.model.layer;

import com.kr.formdang.enums.*;
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
            return FormTypeEnum.SURVEY_TYPE.getCode();
        } else if (this.type == QUIZ) {
            return FormTypeEnum.QUIZ_TYPE.getCode();
        }
        return null;
    }

    public Integer getOrder() {
        final int RECENT = 0, LAST = 2;
        if (this.order == RECENT) {
            return FormOrderEnum.RECENT.getCode();
        } else if (this.order == LAST) {
            return FormOrderEnum.LAST.getCode();
        }
        return null;
    }

    public Integer getEndFlag() {
        final int PROGRESS = 0, END = 1, TEMP = 2, DEL = 3;
        if (this.status == PROGRESS) {
            return FormEndFlagEnum.PROGRESS.getCode();
        } else if (this.status == END) {
            return FormEndFlagEnum.END.getCode();
        } else if (this.status == TEMP) {
            return FormEndFlagEnum.PROGRESS.getCode();
        } else if (this.status == DEL) {
            return null;
        }
        return null;
    }

    public Integer getDelFlag() {
        final int PROGRESS = 0, END = 1, TEMP = 2, DEL = 3;
        if (this.status == PROGRESS) {
            return FormDelFlagEnum.NOT_DEL.getCode();
        } else if (this.status == END) {
            return FormDelFlagEnum.NOT_DEL.getCode();
        } else if (this.status == TEMP) {
            return FormDelFlagEnum.NOT_DEL.getCode();
        } else if (this.status == DEL) {
            return FormDelFlagEnum.DEL.getCode();
        }
        return null;
    }

    public Integer getStatus() {
        final int PROGRESS = 0, END = 1, TEMP = 2, DEL = 3;
        if (this.status == PROGRESS) {
            return FormStatusEnum.NORMAL_STATUS.getCode();
        } else if (this.status == END) {
            return null;
        } else if (this.status == TEMP) {
            return FormStatusEnum.TEMP_STATUS.getCode();
        } else if (this.status == DEL) {
            return null;
        }
        return null;
    }

}
