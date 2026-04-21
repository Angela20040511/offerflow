package com.offerflow.security;

import com.offerflow.exception.BusinessException;

public final class SecurityUtils {
    private SecurityUtils() {
    }

    public static LoginUser currentUser() {
        LoginUser loginUser = UserContext.get();
        if (loginUser == null) {
            throw new BusinessException(401, "登录状态已失效，请重新登录");
        }
        return loginUser;
    }

    public static LoginUser optionalUser() {
        return UserContext.get();
    }

    public static Long currentUserId() {
        return currentUser().getUserId();
    }

    public static Long currentUserIdOrNull() {
        LoginUser loginUser = UserContext.get();
        return loginUser == null ? null : loginUser.getUserId();
    }

    public static boolean isLoggedIn() {
        return UserContext.get() != null;
    }

    public static void requireRole(String role) {
        if (!matchesRole(currentUser().getRole(), role)) {
            throw new BusinessException(403, "当前账号无权执行该操作");
        }
    }

    public static boolean matchesRole(String actualRole, String expectedRole) {
        String actual = normalizeRole(actualRole);
        String expected = normalizeRole(expectedRole);
        return !actual.isEmpty() && actual.equals(expected);
    }

    public static boolean isApplicantRole(String role) {
        return "APPLICANT".equals(normalizeRole(role));
    }

    public static boolean isHrRole(String role) {
        String normalized = normalizeRole(role);
        return "HR".equals(normalized) || "RECRUITER".equals(normalized);
    }

    public static String normalizeRole(String role) {
        if (role == null) {
            return "";
        }
        String normalized = role.trim().toUpperCase();
        if (normalized.startsWith("ROLE_")) {
            normalized = normalized.substring(5);
        }
        return normalized;
    }
}
