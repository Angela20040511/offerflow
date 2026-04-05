package com.offerflow.mapper;

import com.offerflow.entity.Resume;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResumeMapper {
    Resume selectDefaultByUserId(@Param("userId") Long userId);
    Resume selectById(@Param("id") Long id);
    List<Resume> selectByUserId(@Param("userId") Long userId);
    int insertResume(Resume resume);
    int updateResume(Resume resume);
    int updateResumePdfUrl(@Param("id") Long id, @Param("pdfUrl") String pdfUrl);
    int clearDefaultByUserId(@Param("userId") Long userId);
    int setDefaultById(@Param("id") Long id);
    int deleteById(@Param("id") Long id);
    long countByUserId(@Param("userId") Long userId);
}
