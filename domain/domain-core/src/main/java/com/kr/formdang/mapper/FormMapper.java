package com.kr.formdang.mapper;

import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.model.SqlFormParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FormMapper {

    List<FormTbEntity> findForms(SqlFormParam params);

    Long findFormsCnt(SqlFormParam params);

}
