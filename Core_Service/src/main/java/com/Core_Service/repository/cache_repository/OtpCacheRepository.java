package com.Core_Service.repository.cache_repository;

import com.Core_Service.model_request.AdminCreateRequest;
import com.Core_Service.model_request.ViewerCreateRequest;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Repository;

@Repository
@EnableCaching
@CacheConfig(cacheNames = "otp_cache", cacheManager = "customCacheManager")
public class OtpCacheRepository {

    @CachePut(key = "'otp::' + #otp")
    public AdminCreateRequest storeAdminCreateRequest(String otp, AdminCreateRequest adminCreateRequest) {
        return adminCreateRequest;
    }

    @CachePut(key = "'otp::' + #otp")
    public ViewerCreateRequest storeViewerCreateRequest(String otp, ViewerCreateRequest viewerCreateRequest) {
        return viewerCreateRequest;
    }

    @Cacheable(key = "'otp::' + #otp")
    public AdminCreateRequest getAdminCreateRequest(String otp) {
        return null;
    }

    @Cacheable(key = "'otp::' + #otp")
    public ViewerCreateRequest getViewerCreateRequest(String otp) {
        return null;
    }

    @CacheEvict(key = "'otp::' + #otp")
    public void deleteAdminCreateRequest(String otp) {}

    @CacheEvict(key = "'otp::' + #otp")
    public void deleteViewerCreateRequest(String otp) {}

}
