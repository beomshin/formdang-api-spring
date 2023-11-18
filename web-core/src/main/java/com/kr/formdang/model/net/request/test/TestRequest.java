package com.kr.formdang.model.net.request.test;

import com.kr.formdang.model.root.DefaultRequest;
import com.kr.formdang.model.root.RootRequest;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TestRequest extends DefaultRequest {

    private String name;
    private String age;

}
