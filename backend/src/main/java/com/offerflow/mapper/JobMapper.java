package com.offerflow.mapper;

import com.offerflow.entity.Job;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface JobMapper {
    long countJobPage(Map<String, Object> params);
    List<Map<String, Object>> selectJobPage(Map<String, Object> params);
    long countHrJobPage(Map<String, Object> params);
    List<Map<String, Object>> selectHrJobPage(Map<String, Object> params);
    Job selectById(@Param("id") Long id);
    Map<String, Object> selectJobDetail(@Param("id") Long id);
    int insertJob(Job job);
    int updateJob(Job job);
    int updateJobStatus(@Param("id") Long id, @Param("status") String status);
    int deleteById(@Param("id") Long id);
    long countOpenJobs();
    List<Map<String, Object>> selectSubsidiaryJobDistribution();
}
