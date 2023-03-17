package com.cpm.account.dto.account;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountTransferRequest {
    private Long sourceAccountId;
    private Long targetAccountId;
    @NotNull
    private BigDecimal amount;
}
