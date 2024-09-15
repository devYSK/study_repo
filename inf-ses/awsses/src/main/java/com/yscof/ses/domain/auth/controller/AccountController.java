package com.yscof.ses.domain.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yscof.ses.domain.auth.model.request.SendOTPRequest;
import com.yscof.ses.domain.auth.model.response.SendOTPResponse;
import com.yscof.ses.domain.auth.service.OTPService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Acceount API", description = "계정 관련 API")
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    
    private final OTPService otpService;

    @Operation(
        summary = "Email에 OTP 전송",
        description = "Email에 대해서 OTP를 전송합니다."
    )
    @GetMapping("/make-user/{email}")
    public SendOTPResponse sendOTP(
        @RequestBody @Valid SendOTPRequest request
    ) {
        return otpService.sendOTP(request);
    }
}
