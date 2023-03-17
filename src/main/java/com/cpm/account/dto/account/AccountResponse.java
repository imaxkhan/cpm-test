package com.cpm.account.dto.account;

import com.cpm.account.dto.transaction.AccountTransactionResponse;
import com.cpm.account.entity.AccountEntity;
import com.cpm.account.entity.AccountTransactionEntity;
import com.cpm.account.statics.TransactionType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class AccountResponse {
    private Long id;
    private LocalDateTime created;
    private BigDecimal balance;
    private List<AccountTransactionResponse> transactions;

    public AccountResponse(AccountEntity entity) {
        this.id = entity.getId();
        this.created = entity.getCreated();
        if (Hibernate.isInitialized(entity.getTransactions()) && entity.getTransactions() != null) {
            this.balance = BigDecimal.ZERO;
            this.transactions = new ArrayList<>();
            for (AccountTransactionEntity eachTransaction : entity.getTransactions()) {
                AccountTransactionResponse transaction = new AccountTransactionResponse(eachTransaction);
                if (transaction.getType().equals(TransactionType.DEPOSIT)) {
                    this.balance = this.balance.add(transaction.getAmount());
                } else {
                    this.balance = this.balance.subtract(transaction.getAmount());
                }
                this.transactions.add(transaction);
            }
        }
    }
}
