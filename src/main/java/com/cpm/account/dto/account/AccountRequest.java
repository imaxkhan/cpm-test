package com.cpm.account.dto.account;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
public class AccountRequest {
    @NotNull
    private Long customerId;
    @NotNull
    private BigDecimal initialCredit;
}
