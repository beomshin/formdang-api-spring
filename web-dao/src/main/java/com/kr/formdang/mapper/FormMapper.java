package com.kr.formdang.mapper;

import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.model.dao.form.FindFormDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface FormMapper {

    List<FormTbEntity> findForms(FindFormDto params);

    Long findFormsCnt(FindFormDto params);

}
