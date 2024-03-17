package com.ys.membershipservice.application.port.out;

import com.ys.membershipservice.domain.Membership;

public interface AuthMembershipPort {
    // membership id 를 기준으로, jwt token 을 생성한다.
    String generateJwtToken(
            Membership.MembershipId membershipId
    );

    // membership id 를 기준으로, refresh token 을 생성한다.
    String generateRefreshToken(
            Membership.MembershipId membershipId
    );

    // jwtToken 이 유효한지 확인
    boolean validateJwtToken(String jwtToken);

    // jwtToken 으로, 어떤 멤버십 id 를 가지는지 확인
    Membership.MembershipId parseMembershipIdFromToken(String jwtToken);
}
