package com.kr.formdang.external;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface HttpFormClient {

    <T> T requestToken(Object content, Class<T> tClass) throws JsonProcessingException;

    <T> T requestUserInfo(String token, Class<T> tClass) throws IllegalAccessException, JsonProcessingException;
}
