package com.kr.formdang.mapper;

import com.kr.formdang.dto.FormTbDto;
import com.kr.formdang.dto.SqlFormParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FormMapper {

    List<FormTbDto> findForms(SqlFormParam params);

    Long findFormsCnt(SqlFormParam params);

}
