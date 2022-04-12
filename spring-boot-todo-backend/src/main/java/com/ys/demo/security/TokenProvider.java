package com.ys.demo.security;

import com.ys.demo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Slf4j
public class TokenProvider {
    private static final String SECRET_KEY = "NMA8JpctFuna59f5";

    public String create(UserEntity userEntity) {
        // 기간은 1일
        Date expiryDate = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                //header 및 서명 키
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                //payload
                .setSubject(userEntity.getId()) // sub
                .setIssuer("demo ys app")
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) // exp
                .compact();
    }

    public String validateAndGetUserId(String token) {
        /**
         *  parseClaimsJws 메서드가 Base64로 디코딩 및 파싱
         *  헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명한 후 token의 서명과 비교
         *  위조되지 않았다면 페이로드(claims) 리턴. 위조라면 예외
         *  그중 우리는 userId가 필요하므로 getBody 부름
         */

        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
