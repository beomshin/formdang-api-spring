package com.kr.formdang.model.layer;

import com.kr.formdang.model.net.request.AdminSignRequest;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AdminDataDto {

    private String id;
    private String pw;

    public AdminDataDto(AdminSignRequest request) {
        this.id = request.getId();
        this.pw = request.getPw();
    }
}
