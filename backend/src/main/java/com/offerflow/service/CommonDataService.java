package com.offerflow.service;

import com.offerflow.mapper.JobCategoryMapper;
import com.offerflow.mapper.RegionCityMapper;
import com.offerflow.mapper.RegionProvinceMapper;
import com.offerflow.mapper.SubsidiaryCompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommonDataService {
    private final SubsidiaryCompanyMapper subsidiaryCompanyMapper;
    private final JobCategoryMapper jobCategoryMapper;
    private final RegionProvinceMapper regionProvinceMapper;
    private final RegionCityMapper regionCityMapper;

    public List<Map<String, Object>> listSubsidiaries() {
        return subsidiaryCompanyMapper.selectActiveList().stream().map(item -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", item.getId());
            row.put("groupName", item.getGroupName());
            row.put("subsidiaryName", item.getSubsidiaryName());
            row.put("shortName", item.getShortName());
            row.put("businessLine", item.getBusinessLine());
            row.put("intro", item.getIntro());
            row.put("sortNum", item.getSortNum());
            return row;
        }).toList();
    }

    public List<Map<String, Object>> listJobCategories() {
        return jobCategoryMapper.selectActiveList().stream().map(item -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", item.getId());
            row.put("categoryName", item.getCategoryName());
            row.put("parentId", item.getParentId());
            row.put("sortNum", item.getSortNum());
            return row;
        }).toList();
    }

    public List<Map<String, Object>> listProvinces() {
        return regionProvinceMapper.selectAll().stream().map(item -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("code", item.getCode());
            row.put("name", item.getName());
            row.put("sortNum", item.getSortNum());
            return row;
        }).toList();
    }

    public List<Map<String, Object>> listCities(String provinceCode) {
        if (provinceCode == null || provinceCode.isBlank()) {
            return List.of();
        }
        return regionCityMapper.selectByProvinceCode(provinceCode).stream().map(item -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("code", item.getCode());
            row.put("provinceCode", item.getProvinceCode());
            row.put("name", item.getName());
            row.put("sortNum", item.getSortNum());
            return row;
        }).toList();
    }
}
