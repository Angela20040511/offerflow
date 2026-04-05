package com.offerflow.mapper;

import com.offerflow.entity.GuideDoc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GuideDocMapper {
    List<GuideDoc> selectAllOrderBySort();
    GuideDoc selectByCode(@Param("code") String code);
}
