package com.kr.formdang.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kr.formdang.exception.FormHttpException;

public interface HttpFormClient {

    <T> T requestToken(Object content, Class<T> tClass) throws JsonProcessingException, FormHttpException;

    <T> T requestUserInfo(String token, Class<T> tClass) throws IllegalAccessException, JsonProcessingException, FormHttpException;
}
