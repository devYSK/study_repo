package com.fastcampuspay.banking.application.port.in;


import com.fastcampuspay.banking.domain.RegisteredBankAccount;
import com.fastcampuspay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class GetRegisteredBankAccountCommand extends SelfValidating<GetRegisteredBankAccountCommand> {
    @NotNull
    private final String membershipId;

    public GetRegisteredBankAccountCommand(String membershipId) {
        this.membershipId = membershipId;
        this.validateSelf();
    }
}
