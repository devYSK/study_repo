package com.fastcampus.housebatch.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * 아파트 실거래가 API를 호출하기 위한 파라미터
 * 1. serviceKey - API를 호출하기 위한 인증키
 * 2. LAWD_CD - 법정동 코드 10자리 중 앞 5자리 - 구 지역코드 guLawdCd. 예) 41135
 * 3. DEAL_YMD - 거래가 발생한 년월. 예) 202107
 */
@Slf4j
@Component
public class ApartmentApiResource {
    @Value("${external.apartment-api.path}")
    private String path;

    @Value("${external.apartment-api.service-key}")
    private String serviceKey;

    public Resource getResource(String lawdCd, YearMonth yearMonth) {
        String url = String.format("%s?serviceKey=%s&LAWD_CD=%s&DEAL_YMD=%s", path, serviceKey, lawdCd,
                yearMonth.format(DateTimeFormatter.ofPattern("yyyyMM")));

        log.info("Resource URL = " + url);

        try {
            return new UrlResource(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Failed to created UrlResource");
        }
    }
}
