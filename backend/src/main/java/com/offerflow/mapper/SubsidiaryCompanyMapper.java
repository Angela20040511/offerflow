package com.offerflow.mapper;

import com.offerflow.entity.SubsidiaryCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubsidiaryCompanyMapper {
    List<SubsidiaryCompany> selectActiveList();
    SubsidiaryCompany selectById(@Param("id") Long id);
}
