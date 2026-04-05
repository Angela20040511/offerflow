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
        if (!role.equals(currentUser().getRole())) {
            throw new BusinessException(403, "当前账号无权执行该操作");
        }
    }
}
