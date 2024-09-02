package com.Core_Service.service;

import com.Core_Service.model_request.AdminCreateRequest;
import com.Core_Service.model_request.ViewerCreateRequest;
import com.Core_Service.model_response.UserResponse;
import com.Core_Service.repository.cache_repository.OtpCacheRepository;
import org.commonDTO.OtpNotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

@Service
public class AccountCreationService {

    @Autowired
    private SecureRandom random;

    @Value("${otp.digits}")
    private String digits;

    @Value("${otp.length}")
    private String otpLength;

    @Autowired
    private OtpCacheRepository otpCacheRepository;

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ViewerService viewerService;

    private String generateOTP() {
        int length = Integer.parseInt(otpLength);
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(digits.length());
            otp.append(digits.charAt(index));
        }
        return otp.toString();
    }

    public boolean generateAdminCreateRequest(AdminCreateRequest adminCreateRequest) {
        String otp = generateOTP();
        otpCacheRepository.storeAdminCreateRequest(otp, adminCreateRequest);
        streamBridge.send("OTPMessageTopic", OtpNotificationMessage.builder().username(adminCreateRequest.getUsername())
                .message("Account Creation OTP: " + otp + "\nPlease Don't share this otp with anyone.")
                .build());
        return true;
    }

    public boolean generateViewerCreateRequest(ViewerCreateRequest viewerCreateRequest) {
        String otp = generateOTP();
        otpCacheRepository.storeViewerCreateRequest(otp, viewerCreateRequest);
        streamBridge.send("OTPMessageTopic", OtpNotificationMessage.builder().username(viewerCreateRequest.getUsername())
                .message("Account Creation OTP: " + otp + "\nPlease Don't share this otp with anyone.")
                .build());
        return true;
    }

    public UserResponse verifyOtpAdmin(String otp) {
        AdminCreateRequest adminCreateRequest = otpCacheRepository.getAdminCreateRequest(otp);
        if(adminCreateRequest != null) {
            otpCacheRepository.deleteAdminCreateRequest(otp);
            return adminService.createAdmin(adminCreateRequest);
        }
        throw new VerifyError("OTP verification failed !!!");
    }

    public UserResponse verifyOtpViewer(String otp) {
        ViewerCreateRequest viewerCreateRequest = otpCacheRepository.getViewerCreateRequest(otp);
        if(viewerCreateRequest != null) {
            otpCacheRepository.deleteViewerCreateRequest(otp);
            return viewerService.createViewer(viewerCreateRequest);
        }
        throw new VerifyError("OTP verification failed !!!");
    }

}
