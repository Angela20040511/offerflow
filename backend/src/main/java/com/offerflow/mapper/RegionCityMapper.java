package com.offerflow.mapper;

import com.offerflow.entity.RegionCity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RegionCityMapper {
    List<RegionCity> selectByProvinceCode(@Param("provinceCode") String provinceCode);
    RegionCity selectByCode(@Param("code") String code);
}
