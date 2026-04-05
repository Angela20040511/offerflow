package com.offerflow.service;

import com.offerflow.common.PageResult;
import com.offerflow.dto.ApiModels;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicationService applicationService;

    public Map<String, Object> overview() {
        return applicationService.overview();
    }

    public Map<String, Object> apply(ApiModels.ApplicationCreateRequest request) {
        return applicationService.apply(request);
    }

    public PageResult<Map<String, Object>> myApplications(ApiModels.ApplicationPageQuery query) {
        return applicationService.myApplications(query);
    }

    public Boolean withdraw(Long applicationId) {
        return applicationService.withdraw(applicationId);
    }

    public Boolean addFavorite(ApiModels.FavoriteCreateRequest request) {
        return applicationService.addFavorite(request);
    }

    public Boolean removeFavorite(Long jobId) {
        return applicationService.removeFavorite(jobId);
    }

    public PageResult<Map<String, Object>> myFavorites(ApiModels.FavoritePageQuery query) {
        return applicationService.myFavorites(query);
    }
}
