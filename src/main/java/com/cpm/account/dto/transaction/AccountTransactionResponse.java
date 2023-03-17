package com.cpm.account.dto.transaction;

import com.cpm.account.entity.AccountTransactionEntity;
import com.cpm.account.statics.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class AccountTransactionResponse {
    private Long id;
    private LocalDateTime created;
    private BigDecimal amount;
    private TransactionType type;

    public AccountTransactionResponse(AccountTransactionEntity entity){
        this.id = entity.getId();
        this.created = entity.getCreated();
        this.amount=entity.getAmount();
        this.type = entity.getType();
    }
}
