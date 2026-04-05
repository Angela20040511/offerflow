package com.offerflow.service;

import com.offerflow.common.PageResult;
import com.offerflow.dto.JobQueryDTO;
import com.offerflow.dto.JobSaveDTO;
import com.offerflow.entity.Application;
import com.offerflow.entity.Job;
import com.offerflow.entity.JobCategory;
import com.offerflow.entity.RegionCity;
import com.offerflow.entity.RegionProvince;
import com.offerflow.entity.SubsidiaryCompany;
import com.offerflow.exception.BusinessException;
import com.offerflow.mapper.ApplicationMapper;
import com.offerflow.mapper.FavoriteMapper;
import com.offerflow.mapper.JobCategoryMapper;
import com.offerflow.mapper.JobMapper;
import com.offerflow.mapper.RegionCityMapper;
import com.offerflow.mapper.RegionProvinceMapper;
import com.offerflow.mapper.SubsidiaryCompanyMapper;
import com.offerflow.security.SecurityUtils;
import com.offerflow.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobService {
    private static final String DEFAULT_GROUP_NAME = "用友集团";

    private final JobMapper jobMapper;
    private final FavoriteMapper favoriteMapper;
    private final ApplicationMapper applicationMapper;
    private final SubsidiaryCompanyMapper subsidiaryCompanyMapper;
    private final JobCategoryMapper jobCategoryMapper;
    private final RegionProvinceMapper regionProvinceMapper;
    private final RegionCityMapper regionCityMapper;

    public PageResult<Map<String, Object>> page(JobQueryDTO query) {
        Map<String, Object> params = buildParams(query, true);
        long total = jobMapper.countJobPage(params);
        List<Map<String, Object>> list = jobMapper.selectJobPage(params).stream().map(this::normalizeJobRow).toList();
        return new PageResult<>(list, total, query.getPageNum(), query.getPageSize());
    }

    public Map<String, Object> detail(Long id) {
        Map<String, Object> detail = jobMapper.selectJobDetail(id);
        if (detail == null) {
            throw new BusinessException(404, "岗位不存在");
        }
        Map<String, Object> result = normalizeJobRow(detail);
        Map<String, Object> companyInfo = new LinkedHashMap<>();
        companyInfo.put("groupName", result.get("groupName"));
        companyInfo.put("subsidiaryName", result.get("subsidiaryName"));
        companyInfo.put("businessLine", result.get("businessLine"));
        companyInfo.put("city", result.get("cityName"));
        result.put("companyInfo", companyInfo);
        return result;
    }

    public PageResult<Map<String, Object>> hrPage(JobQueryDTO query) {
        SecurityUtils.requireRole("HR");
        Map<String, Object> params = buildParams(query, false);
        long total = jobMapper.countHrJobPage(params);
        List<Map<String, Object>> list = jobMapper.selectHrJobPage(params).stream().map(this::normalizeJobRow).toList();
        return new PageResult<>(list, total, query.getPageNum(), query.getPageSize());
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(JobSaveDTO request) {
        SecurityUtils.requireRole("HR");
        Job job = toJob(null, request);
        jobMapper.insertJob(job);
        return job.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean update(Long id, JobSaveDTO request) {
        SecurityUtils.requireRole("HR");
        Job existing = jobMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "岗位不存在");
        }
        Job job = toJob(existing, request);
        job.setId(id);
        return jobMapper.updateJob(job) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        SecurityUtils.requireRole("HR");
        return jobMapper.deleteById(id) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStatus(Long id, String status) {
        SecurityUtils.requireRole("HR");
        return jobMapper.updateJobStatus(id, status) > 0;
    }

    public List<Map<String, Object>> recommendJobs(Long userId, int limit) {
        JobQueryDTO query = new JobQueryDTO();
        query.setPageNum(1);
        query.setPageSize(limit * 3);
        query.setStatus("OPEN");
        query.setSortBy("latest");
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> item : jobMapper.selectJobPage(buildParams(query, true))) {
            Long jobId = asLong(item.get("id"));
            Application application = applicationMapper.selectByUserIdAndJobId(userId, jobId);
            if (application == null) {
                result.add(normalizeJobRow(item));
            }
            if (result.size() >= limit) {
                break;
            }
        }
        return result;
    }

    private Map<String, Object> buildParams(JobQueryDTO query, boolean publicPage) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("keyword", query.getKeyword());
        params.put("subsidiaryId", query.getSubsidiaryId());
        params.put("categoryId", query.getCategoryId());
        params.put("provinceCode", query.getProvinceCode());
        params.put("cityCode", query.getCityCode());
        params.put("city", query.getCity());
        params.put("category", query.getCategory());
        params.put("type", query.getType());
        params.put("workMode", query.getWorkMode());
        params.put("durationType", query.getDurationType());
        params.put("salaryMin", query.getSalaryMin());
        params.put("salaryMax", query.getSalaryMax());
        params.put("sortBy", query.getSortBy());
        params.put("status", publicPage ? (StringUtils.hasText(query.getStatus()) ? query.getStatus() : "OPEN") : query.getStatus());
        params.put("offset", (query.getPageNum() - 1) * query.getPageSize());
        params.put("pageSize", query.getPageSize());
        return params;
    }

    private Map<String, Object> normalizeJobRow(Map<String, Object> source) {
        Map<String, Object> row = new LinkedHashMap<>(source);
        row.put("groupName", valueOrDefault(row.get("groupName"), DEFAULT_GROUP_NAME));
        row.put("company", valueOrDefault(row.get("company"), row.get("subsidiaryName")));
        row.put("city", valueOrDefault(row.get("city"), row.get("cityName")));
        row.put("category", valueOrDefault(row.get("category"), row.get("categoryName")));
        row.put("type", normalizeType(stringValue(valueOrDefault(row.get("type"), row.get("durationType")))));
        row.put("workMode", normalizeWorkMode(stringValue(valueOrDefault(row.get("workMode"), "ONSITE"))));
        row.put("durationType", normalizeDurationType(stringValue(valueOrDefault(row.get("durationType"), row.get("type")))));
        row.put("tags", JsonUtils.readStringList(stringValue(row.get("tags"))));
        row.put("requiredSkills", JsonUtils.readStringList(stringValue(row.get("requiredSkillsJson"))));
        Long jobId = asLong(row.get("id"));
        Long userId = SecurityUtils.currentUserIdOrNull();
        boolean isFavorite = false;
        boolean isApplied = false;
        if (userId != null && jobId != null) {
            isFavorite = favoriteMapper.existsFavorite(userId, jobId) > 0;
            isApplied = applicationMapper.selectByUserIdAndJobId(userId, jobId) != null;
        }
        row.put("isFavorite", isFavorite);
        row.put("isApplied", isApplied);
        return row;
    }

    private Job toJob(Job existing, JobSaveDTO request) {
        SubsidiaryCompany subsidiary = resolveSubsidiary(request.getSubsidiaryId(), existing == null ? null : existing.getSubsidiaryId());
        JobCategory category = resolveCategory(request.getJobCategoryId(), existing == null ? null : existing.getJobCategoryId());
        RegionProvince province = resolveProvince(request.getProvinceCode(), existing == null ? null : existing.getProvinceCode());
        RegionCity city = resolveCity(request.getCityCode(), province.getCode(), existing == null ? null : existing.getCityCode());

        String businessLine = firstText(request.getBusinessLine(), existing == null ? null : existing.getBusinessLine(), subsidiary.getBusinessLine());
        String company = firstText(request.getCompany(), existing == null ? null : existing.getCompany(), subsidiary.getSubsidiaryName());
        String cityName = firstText(request.getCity(), existing == null ? null : existing.getCity(), city.getName());
        String categoryName = firstText(request.getCategory(), existing == null ? null : existing.getCategory(), category.getCategoryName());
        String type = normalizeType(firstText(request.getType(), existing == null ? null : existing.getType(), request.getDurationType(), existing == null ? null : existing.getDurationType(), "LONG_TERM"));
        String workMode = normalizeWorkMode(firstText(request.getWorkMode(), existing == null ? null : existing.getWorkMode(), "ONSITE"));
        String durationType = normalizeDurationType(firstText(request.getDurationType(), existing == null ? null : existing.getDurationType(), type, "LONG_TERM"));
        String internshipLength = firstText(request.getInternshipLength(), existing == null ? null : existing.getInternshipLength());

        return Job.builder()
                .id(existing == null ? null : existing.getId())
                .subsidiaryId(subsidiary.getId())
                .jobCategoryId(category.getId())
                .title(request.getTitle())
                .company(company)
                .city(cityName)
                .category(categoryName)
                .type(type)
                .businessLine(businessLine)
                .provinceCode(province.getCode())
                .cityCode(city.getCode())
                .workMode(workMode)
                .durationType(durationType)
                .internshipLength(internshipLength)
                .requiredSkillsJson(JsonUtils.toJson(request.getRequiredSkills()))
                .tags(JsonUtils.toJson(request.getTags().isEmpty() ? request.getRequiredSkills() : request.getTags()))
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .description(request.getDescription())
                .requirement(request.getRequirement())
                .status(request.getStatus())
                .publishTime(existing == null ? ("OPEN".equals(request.getStatus()) ? LocalDateTime.now() : null) : existing.getPublishTime())
                .build();
    }

    private SubsidiaryCompany resolveSubsidiary(Long requestId, Long fallbackId) {
        Long targetId = requestId != null ? requestId : fallbackId;
        SubsidiaryCompany company = targetId == null ? null : subsidiaryCompanyMapper.selectById(targetId);
        if (company != null) {
            return company;
        }
        return subsidiaryCompanyMapper.selectActiveList().stream().findFirst().orElseThrow(() -> new BusinessException(500, "未配置可用子公司"));
    }

    private JobCategory resolveCategory(Long requestId, Long fallbackId) {
        Long targetId = requestId != null ? requestId : fallbackId;
        JobCategory category = targetId == null ? null : jobCategoryMapper.selectById(targetId);
        if (category != null) {
            return category;
        }
        return jobCategoryMapper.selectActiveList().stream().findFirst().orElseThrow(() -> new BusinessException(500, "未配置可用岗位类别"));
    }

    private RegionProvince resolveProvince(String requestCode, String fallbackCode) {
        String targetCode = StringUtils.hasText(requestCode) ? requestCode : fallbackCode;
        List<RegionProvince> list = regionProvinceMapper.selectAll();
        if (StringUtils.hasText(targetCode)) {
            for (RegionProvince item : list) {
                if (targetCode.equals(item.getCode())) {
                    return item;
                }
            }
        }
        return list.stream().findFirst().orElseThrow(() -> new BusinessException(500, "未配置省份数据"));
    }

    private RegionCity resolveCity(String requestCode, String provinceCode, String fallbackCode) {
        String targetCode = StringUtils.hasText(requestCode) ? requestCode : fallbackCode;
        RegionCity city = StringUtils.hasText(targetCode) ? regionCityMapper.selectByCode(targetCode) : null;
        if (city != null) {
            return city;
        }
        return regionCityMapper.selectByProvinceCode(provinceCode).stream().findFirst().orElseThrow(() -> new BusinessException(500, "未配置城市数据"));
    }

    private String firstText(String... values) {
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value.trim();
            }
        }
        return "";
    }

    private String normalizeType(String value) {
        String normalized = normalizeDurationType(value);
        return StringUtils.hasText(normalized) ? normalized : "LONG_TERM";
    }

    private String normalizeWorkMode(String value) {
        if (!StringUtils.hasText(value)) {
            return "ONSITE";
        }
        return switch (value.trim().toUpperCase()) {
            case "REMOTE", "远程协同", "远程办公" -> "REMOTE";
            case "HYBRID", "混合办公", "混合协同" -> "HYBRID";
            case "ONSITE", "现场办公", "线下办公" -> "ONSITE";
            default -> "ONSITE";
        };
    }

    private String normalizeDurationType(String value) {
        if (!StringUtils.hasText(value)) {
            return "LONG_TERM";
        }
        return switch (value.trim().toUpperCase()) {
            case "SHORT_TERM", "短期实习" -> "SHORT_TERM";
            case "FULL_TIME", "全职", "校招全职" -> "FULL_TIME";
            case "LONG_TERM", "长期实习", "实习" -> "LONG_TERM";
            default -> "LONG_TERM";
        };
    }

    private Long asLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.parseLong(String.valueOf(value));
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private Object valueOrDefault(Object value, Object defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        String text = String.valueOf(value);
        return text.isBlank() ? defaultValue : value;
    }
}
