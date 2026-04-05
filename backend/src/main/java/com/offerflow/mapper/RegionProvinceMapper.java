package com.offerflow.mapper;

import com.offerflow.entity.RegionProvince;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RegionProvinceMapper {
    List<RegionProvince> selectAll();
}
