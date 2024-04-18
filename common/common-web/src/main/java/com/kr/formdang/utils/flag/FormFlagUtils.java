package com.kr.formdang.utils.flag;

import com.kr.formdang.enums.*;

public class FormFlagUtils {

    public static Integer delFlag(Integer status) {
        final int PROGRESS = 0, END = 1, TEMP = 2, DEL = 3;
        if (status == PROGRESS) {
            return FormDelFlagEnum.NOT_DEL.getCode();
        } else if (status == END) {
            return FormDelFlagEnum.NOT_DEL.getCode();
        } else if (status == TEMP) {
            return FormDelFlagEnum.NOT_DEL.getCode();
        } else if (status == DEL) {
            return FormDelFlagEnum.DEL.getCode();
        }
        return null;
    }


    public static Integer endFlag(Integer status) {
        final int PROGRESS = 0, END = 1, TEMP = 2, DEL = 3;
        if (status == PROGRESS) {
            return FormEndFlagEnum.PROGRESS.getCode();
        } else if (status == END) {
            return FormEndFlagEnum.END.getCode();
        } else if (status == TEMP) {
            return FormEndFlagEnum.PROGRESS.getCode();
        } else if (status == DEL) {
            return null;
        }
        return null;
    }

    public static Integer type(Integer type) {
        final int ALL = 99, SURVEY = 0, QUIZ = 1;
        if (type == ALL) {
            return null;
        } else if (type == SURVEY) {
            return FormTypeEnum.SURVEY_TYPE.getCode();
        } else if (type == QUIZ) {
            return FormTypeEnum.QUIZ_TYPE.getCode();
        }
        return null;
    }

    public static Integer status(Integer status) {
        final int PROGRESS = 0, END = 1, TEMP = 2, DEL = 3;
        if (status == PROGRESS) {
            return FormStatusEnum.NORMAL_STATUS.getCode();
        } else if (status == END) {
            return null;
        } else if (status == TEMP) {
            return FormStatusEnum.TEMP_STATUS.getCode();
        } else if (status == DEL) {
            return null;
        }
        return null;
    }


    public static Integer order(Integer order) {
        final int RECENT = 0, MANY_ANSWER = 1, LAST = 2;
        if (order == RECENT) {
            return FormOrderEnum.RECENT.getCode();
        } else if (order == MANY_ANSWER) {
            return FormOrderEnum.MANY_RESPONSE.getCode();
        } else if (order == LAST) {
            return FormOrderEnum.LAST.getCode();
        }
        return null;
    }

}
