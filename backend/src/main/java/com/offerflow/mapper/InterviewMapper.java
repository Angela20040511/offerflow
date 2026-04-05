package com.offerflow.mapper;

import com.offerflow.entity.Interview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InterviewMapper {
    List<Interview> selectByApplicationId(@Param("applicationId") Long applicationId);
    int insertInterview(Interview interview);
    int updateInterviewStatus(@Param("id") Long id, @Param("status") String status, @Param("remark") String remark);
}
