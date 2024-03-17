package com.ys.membershipservice.application.port.in;

import com.ys.membershipservice.domain.JwtToken;
import com.ys.membershipservice.domain.Membership;

public interface AuthMembershipUseCase {
    JwtToken loginMembership(LoginMembershipCommand command);

    JwtToken refreshJwtTokenByRefreshToken(RefreshTokenCommand command);
    boolean validateJwtToken(ValidateTokenCommand command);

    Membership getMembershipByJwtToken(ValidateTokenCommand command);
}
