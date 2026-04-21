package com.offerflow.mapper;

import com.offerflow.entity.Application;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApplicationMapper {
    int insertApplication(Application application);
    Application selectByUserIdAndJobId(@Param("userId") Long userId, @Param("jobId") Long jobId);
    Application selectById(@Param("id") Long id);
    long countMyApplicationPage(Map<String, Object> params);
    List<Map<String, Object>> selectMyApplicationPage(Map<String, Object> params);
    int withdrawApplication(@Param("id") Long id, @Param("userId") Long userId);
    int reapplyApplication(@Param("id") Long id,
                           @Param("resumeId") Long resumeId,
                           @Param("matchScore") Integer matchScore,
                           @Param("systemMatchScore") Integer systemMatchScore);
    long countHrApplicationPage(Map<String, Object> params);
    List<Map<String, Object>> selectHrApplicationPage(Map<String, Object> params);
    long countCandidatePage(Map<String, Object> params);
    List<Map<String, Object>> selectCandidatePage(Map<String, Object> params);
    Map<String, Object> selectCandidateDetailByApplicationId(@Param("applicationId") Long applicationId);
    int updateApplicationStage(@Param("applicationId") Long applicationId, @Param("stage") String stage);
    int updateHrNote(@Param("applicationId") Long applicationId, @Param("hrNote") String hrNote);
    int updateHrReview(@Param("applicationId") Long applicationId,
                       @Param("hrReviewScore") Integer hrReviewScore,
                       @Param("hrReviewStatus") String hrReviewStatus,
                       @Param("hrNote") String hrNote);
    long countByResumeId(@Param("resumeId") Long resumeId);
    long countApplicantApplications(@Param("userId") Long userId);
    long countApplicantInterview(@Param("userId") Long userId);
    long countByStage(@Param("stage") String stage);
    long countNewApplicationsToday();
    List<Map<String, Object>> selectStageStats();
    List<Map<String, Object>> selectSubsidiaryApplicationDistribution();
}
