package com.yscof.ses.domain.auth.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "OPT 전송 결과값")
public record SendOTPResponse(
    @Schema(description = "Email")
    String email
) {} 
