package com.offerflow.util;

import java.util.List;
import java.util.Map;

public final class ResumeScoreUtil {
    private ResumeScoreUtil() {
    }

    public static int compute(Map<String, Object> basicInfo,
                              List<Map<String, Object>> educationList,
                              List<Map<String, Object>> experienceList,
                              List<Map<String, Object>> projectList,
                              List<String> skillList,
                              String pdfUrl) {
        int score = 0;
        score += hasText(basicInfo.get("name")) ? 12 : 0;
        score += hasText(basicInfo.get("phone")) ? 10 : 0;
        score += hasText(basicInfo.get("email")) ? 10 : 0;
        score += hasText(basicInfo.get("school")) ? 12 : 0;
        score += hasText(basicInfo.get("major")) ? 10 : 0;
        score += hasText(basicInfo.get("intention")) ? 10 : 0;
        score += !educationList.isEmpty() ? 12 : 0;
        score += !experienceList.isEmpty() ? 10 : 0;
        score += !projectList.isEmpty() ? 8 : 0;
        score += !skillList.isEmpty() ? 4 : 0;
        score += hasText(pdfUrl) ? 2 : 0;
        return Math.min(score, 100);
    }

    private static boolean hasText(Object value) {
        return value != null && !String.valueOf(value).isBlank();
    }
}
