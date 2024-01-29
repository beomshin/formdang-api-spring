package com.kr.formdang.mapper;

import com.kr.formdang.entity.FormTbEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface FormMapper {

    List<FormTbEntity> findForms(Map<String, Object> params);

    Long findFormsCnt(Map<String, Object> params);

}
