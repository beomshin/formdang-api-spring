package com.kr.formdang.mapper;

import com.kr.formdang.dao.FormTbDto;
import com.kr.formdang.dao.FindFormDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FormMapper {

    List<FormTbDto> findForms(FindFormDto params);

    Long findFormsCnt(FindFormDto params);

}
