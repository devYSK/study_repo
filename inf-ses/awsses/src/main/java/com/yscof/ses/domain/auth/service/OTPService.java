package com.yscof.ses.domain.auth.service;

import static com.yscof.ses.common.constrants.Constrants.*;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.yscof.ses.common.code.ErrorCode;
import com.yscof.ses.common.exception.CustomException;
import com.yscof.ses.common.validator.EmailValidator;
import com.yscof.ses.domain.auth.model.request.SendOTPRequest;
import com.yscof.ses.domain.auth.model.response.SendOTPResponse;
import com.yscof.ses.domain.repository.Entity.User;
import com.yscof.ses.domain.repository.UserRepository;
import com.yscof.ses.security.OTP;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class OTPService {
    
    // Repository
    private final UserRepository userRepository;
    private final MailService mailService;

    public SendOTPResponse sendOTP(SendOTPRequest request) {
        String email = request.email();

        if (!EmailValidator.isValidEmail(email)) {
            throw new CustomException(ErrorCode.NOT_VALID_EMAIL_REQUEST);
        } else {
            User user = userRepository.findByEmail(email).orElseGet(() -> userRepository.save(
                User.builder()
                .email(email)
                .is_valid(false)
                .build()
            ));

            log.info("Get From DB {}", user.getEmail());

            if (!user.getIs_valid()) {
                // OTP 전송
                String link = OTP.generateQRCodeURL(email, SECRET);

                Map<String, String> data = Map.of(
                    "email", email,
                    "link", link
                );

                mailService.sendTemplatedEmail(INVITE_QR_TEMPLATE, data, email);
            }

            return new SendOTPResponse(email);
        }    
    }
}
