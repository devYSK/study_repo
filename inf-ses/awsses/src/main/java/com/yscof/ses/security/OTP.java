package com.yscof.ses.security;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.yscof.ses.common.constrants.Constrants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OTP {

    public static String generateQRCodeURL(String email, String secretKey) {
        return String.format(
            Constrants.QR_SERVER,
            URLEncoder.encode(Constrants.OTP_ISSUER, StandardCharsets.UTF_8),
            URLEncoder.encode(email, StandardCharsets.UTF_8),
            URLEncoder.encode(secretKey, StandardCharsets.UTF_8),
            URLEncoder.encode(Constrants.OTP_ISSUER, StandardCharsets.UTF_8)
        );
    }

}
