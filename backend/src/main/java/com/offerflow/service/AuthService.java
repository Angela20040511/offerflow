package com.offerflow.service;

import com.offerflow.dto.ApiModels;
import com.offerflow.entity.Resume;
import com.offerflow.entity.User;
import com.offerflow.exception.BusinessException;
import com.offerflow.mapper.ResumeMapper;
import com.offerflow.mapper.UserMapper;
import com.offerflow.security.LoginUser;
import com.offerflow.security.SecurityUtils;
import com.offerflow.security.TokenStore;
import com.offerflow.util.JsonUtils;
import com.offerflow.util.ResumeScoreUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final ResumeMapper resumeMapper;
    private final TokenStore tokenStore;

    public Map<String, Object> login(ApiModels.LoginRequest request) {
        User user = userMapper.selectByUsernameAndRole(request.getUsername(), request.getRole());
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            throw new BusinessException(401, "用户名、密码或角色不正确");
        }
        LoginUser loginUser = tokenStore.create(LoginUser.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .avatar(user.getAvatar())
                .build());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("token", loginUser.getToken());
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("role", user.getRole());
        result.put("avatar", user.getAvatar());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long register(ApiModels.RegisterRequest request) {
        String requestedRole = SecurityUtils.normalizeRole(request.getRole());
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(400, "两次输入的密码不一致");
        }
        if (SecurityUtils.isHrRole(requestedRole)) {
            throw new BusinessException(403, "招聘人员账号暂不支持自助注册");
        }
        if (!SecurityUtils.isApplicantRole(requestedRole)) {
            throw new BusinessException(400, "仅支持注册求职者账号");
        }
        if (userMapper.selectByUsername(request.getUsername()) != null) {
            throw new BusinessException(409, "用户名已存在");
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role("APPLICANT")
                .email(request.getEmail())
                .phone(request.getPhone())
                .avatar("/static/avatar/default.png")
                .status(1)
                .build();
        userMapper.insertUser(user);

        Map<String, Object> basicInfo = new LinkedHashMap<>();
        basicInfo.put("name", request.getUsername());
        basicInfo.put("phone", request.getPhone());
        basicInfo.put("email", request.getEmail());
        basicInfo.put("educationLevel", "");
        basicInfo.put("gender", "");
        Resume resume = Resume.builder()
                .userId(user.getId())
                .title("默认简历")
                .resumeName("默认简历")
                .resumeType("GENERAL")
                .basicInfoJson(JsonUtils.toJson(basicInfo))
                .educationJson(JsonUtils.toJson(List.of()))
                .experienceJson(JsonUtils.toJson(List.of()))
                .projectJson(JsonUtils.toJson(List.of()))
                .skillsJson(JsonUtils.toJson(List.of()))
                .templateCode("classic")
                .pdfUrl("/files/resume/default-resume.pdf")
                .completeScore(ResumeScoreUtil.compute(basicInfo, List.of(), List.of(), List.of(), List.of(), "/files/resume/default-resume.pdf"))
                .isDefault(1)
                .build();
        resumeMapper.insertResume(resume);
        return user.getId();
    }

    public Map<String, Object> profile() {
        User user = userMapper.selectById(SecurityUtils.currentUserId());
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        Resume defaultResume = resumeMapper.selectDefaultByUserId(user.getId());
        String displayName = user.getUsername();
        if (defaultResume != null) {
            String resumeName = String.valueOf(JsonUtils.readObject(defaultResume.getBasicInfoJson()).getOrDefault("name", ""));
            if (!resumeName.isBlank()) {
                displayName = resumeName;
            }
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("displayName", displayName);
        result.put("role", user.getRole());
        result.put("email", user.getEmail());
        result.put("phone", user.getPhone());
        result.put("avatar", user.getAvatar());
        return result;
    }
}
