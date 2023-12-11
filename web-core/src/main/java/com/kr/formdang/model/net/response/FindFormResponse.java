package com.kr.formdang.model.net.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.model.root.DefaultResponse;
import lombok.*;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class FindFormResponse extends DefaultResponse {

    private List list;
    private Long totalElements;
    private Integer totalPage;
    private Integer curPage;

    public FindFormResponse(Page<FormTbEntity> pages) {
        this.list = pages.getContent().stream().map(Forms::new).collect(Collectors.toList());
        this.totalElements = pages.getTotalElements();
        this.totalPage = pages.getTotalPages();
        this.curPage = pages.getNumber();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    static class Forms {
        private Integer type;
        private String title;
        private String logoUrl;

        @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "Asia/Seoul")
        private Timestamp regDt;

        public Forms(FormTbEntity formTbEntity) {
            this.type = formTbEntity.getFormType();
            this.title = formTbEntity.getTitle();
            this.logoUrl = formTbEntity.getLogoUrl();
            this.regDt = formTbEntity.getRegDt();
        }
    }
}
