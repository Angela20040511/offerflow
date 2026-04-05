package com.offerflow.service;

import com.offerflow.entity.GuideDoc;
import com.offerflow.exception.BusinessException;
import com.offerflow.mapper.GuideDocMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GuideService {
    private final GuideDocMapper guideDocMapper;

    public List<Map<String, Object>> list() {
        return guideDocMapper.selectAllOrderBySort().stream().map(this::toMap).toList();
    }

    public Map<String, Object> detail(String code) {
        GuideDoc guideDoc = guideDocMapper.selectByCode(code);
        if (guideDoc == null) {
            throw new BusinessException(404, "指南不存在");
        }
        return toMap(guideDoc);
    }

    private Map<String, Object> toMap(GuideDoc guideDoc) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", guideDoc.getId());
        result.put("code", guideDoc.getCode());
        result.put("title", guideDoc.getTitle());
        result.put("fileUrl", guideDoc.getFileUrl());
        result.put("sortNum", guideDoc.getSortNum());
        return result;
    }
}
