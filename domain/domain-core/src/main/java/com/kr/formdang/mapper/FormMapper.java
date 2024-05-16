package com.kr.formdang.mapper;

import com.kr.formdang.model.FormTbDto;
import com.kr.formdang.model.SqlFormParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FormMapper {

    List<FormTbDto> findForms(SqlFormParam params);

    Long findFormsCnt(SqlFormParam params);

}
