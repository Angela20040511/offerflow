package com.offerflow.mapper;

import com.offerflow.entity.JobCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobCategoryMapper {
    List<JobCategory> selectActiveList();
    JobCategory selectById(@Param("id") Long id);
}
