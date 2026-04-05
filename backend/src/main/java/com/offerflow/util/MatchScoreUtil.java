package com.offerflow.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class MatchScoreUtil {
    private MatchScoreUtil() {
    }

    public static Map<String, Object> buildDetail(String jobTitle,
                                                  String categoryName,
                                                  String businessLine,
                                                  List<String> requiredSkills,
                                                  Map<String, Object> basicInfo,
                                                  List<Map<String, Object>> projects,
                                                  List<Map<String, Object>> experiences,
                                                  List<String> resumeSkills) {
        List<String> normalizedRequiredSkills = sanitize(requiredSkills);
        List<String> normalizedResumeSkills = sanitize(resumeSkills);
        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();
        for (String skill : normalizedRequiredSkills) {
            if (containsSimilar(normalizedResumeSkills, skill)) {
                matchedSkills.add(skill);
            } else {
                missingSkills.add(skill);
            }
        }

        int skillScore;
        if (normalizedRequiredSkills.isEmpty()) {
            skillScore = Math.min(40, normalizedResumeSkills.isEmpty() ? 10 : 16 + normalizedResumeSkills.size() * 4);
        } else {
            double ratio = matchedSkills.size() * 1.0 / normalizedRequiredSkills.size();
            skillScore = Math.min(60, (int) Math.round(ratio * 55) + (normalizedResumeSkills.isEmpty() ? 0 : 5));
        }

        String major = valueOf(basicInfo.get("major"));
        List<String> jobKeywords = buildJobKeywords(jobTitle, categoryName, businessLine, normalizedRequiredSkills);
        int majorScore = scoreMajor(major, jobKeywords);

        String projectText = buildText(projects);
        String experienceText = buildText(experiences);
        List<String> keywordHits = new ArrayList<>();
        for (String keyword : jobKeywords) {
            if (keyword.length() < 2) {
                continue;
            }
            if (containsIgnoreCase(projectText, keyword) || containsIgnoreCase(experienceText, keyword) || containsIgnoreCase(major, keyword)) {
                if (!keywordHits.contains(keyword)) {
                    keywordHits.add(keyword);
                }
            }
        }

        int projectScore = Math.min(24, projects.size() * 4 + keywordHits.size() * 4);
        int experienceScore = Math.min(16, experiences.size() * 4 + Math.min(4, keywordHits.size()));
        int totalScore = Math.min(100, skillScore + majorScore + projectScore + experienceScore);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalScore", totalScore);
        result.put("skillScore", skillScore);
        result.put("majorScore", majorScore);
        result.put("projectScore", projectScore);
        result.put("experienceScore", experienceScore);
        result.put("matchedSkills", matchedSkills);
        result.put("missingSkills", missingSkills);
        result.put("keywordHits", keywordHits);
        return result;
    }

    private static int scoreMajor(String major, List<String> jobKeywords) {
        if (major == null || major.isBlank()) {
            return 0;
        }
        List<String> majorKeywords = new ArrayList<>();
        majorKeywords.addAll(List.of("计算机", "软件", "信息", "网络", "数据", "统计", "数学", "自动化", "电子", "通信", "人工智能", "管理科学", "工业工程"));
        if (jobKeywords.stream().anyMatch(keyword -> containsAny(keyword, List.of("前端", "后端", "开发", "测试", "算法", "java", "vue", "react", "sql", "工程")))) {
            majorKeywords.addAll(List.of("计算机科学", "软件工程", "信息工程", "电子信息", "自动化"));
        }
        if (jobKeywords.stream().anyMatch(keyword -> containsAny(keyword, List.of("数据", "分析", "运营", "商业", "bi", "增长")))) {
            majorKeywords.addAll(List.of("统计学", "数学", "数据科学", "经济学", "金融学"));
        }
        if (jobKeywords.stream().anyMatch(keyword -> containsAny(keyword, List.of("产品", "设计", "交互")))) {
            majorKeywords.addAll(List.of("工业设计", "交互设计", "数字媒体", "心理学", "市场营销"));
        }
        if (majorKeywords.stream().anyMatch(keyword -> containsIgnoreCase(major, keyword))) {
            return 16;
        }
        return 8;
    }

    private static List<String> buildJobKeywords(String jobTitle,
                                                 String categoryName,
                                                 String businessLine,
                                                 List<String> requiredSkills) {
        Set<String> keywords = new LinkedHashSet<>();
        keywords.addAll(splitKeywords(jobTitle));
        keywords.addAll(splitKeywords(categoryName));
        keywords.addAll(splitKeywords(businessLine));
        keywords.addAll(requiredSkills);
        return new ArrayList<>(keywords);
    }

    private static List<String> splitKeywords(String source) {
        if (source == null || source.isBlank()) {
            return List.of();
        }
        String normalized = source
                .replace("/", " ")
                .replace("-", " ")
                .replace("（", " ")
                .replace("）", " ")
                .replace("(", " ")
                .replace(")", " ");
        String[] parts = normalized.split("\\s+");
        List<String> result = new ArrayList<>();
        for (String part : parts) {
            if (part != null && !part.isBlank()) {
                result.add(part.trim());
            }
        }
        return result;
    }

    private static String buildText(List<Map<String, Object>> rows) {
        StringBuilder builder = new StringBuilder();
        for (Map<String, Object> row : rows) {
            for (Object value : row.values()) {
                if (value != null) {
                    builder.append(value).append(' ');
                }
            }
        }
        return builder.toString();
    }

    private static List<String> sanitize(Collection<String> values) {
        List<String> result = new ArrayList<>();
        if (values == null) {
            return result;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                result.add(value.trim());
            }
        }
        return result;
    }

    private static boolean containsSimilar(List<String> source, String keyword) {
        for (String item : source) {
            if (containsIgnoreCase(item, keyword) || containsIgnoreCase(keyword, item)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsIgnoreCase(String source, String keyword) {
        if (source == null || keyword == null) {
            return false;
        }
        return source.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
    }

    private static boolean containsAny(String source, List<String> keywords) {
        for (String keyword : keywords) {
            if (containsIgnoreCase(source, keyword)) {
                return true;
            }
        }
        return false;
    }

    private static String valueOf(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
