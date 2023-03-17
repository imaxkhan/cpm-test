package com.cpm.account.dto.account;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceResponse {
    private Long id;
    private BigDecimal balance;

}
