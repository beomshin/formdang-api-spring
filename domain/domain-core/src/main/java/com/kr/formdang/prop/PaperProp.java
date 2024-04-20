package com.kr.formdang.prop;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PaperProp {

    @Value("${formdang.host}")
    private String host;

    @Value("${formdang.paper}")
    private String paper;

}
