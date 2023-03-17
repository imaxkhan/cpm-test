package com.cpm.account.service;

import com.cpm.account.dto.account.AccountTransferRequest;
import com.cpm.account.entity.AccountTransactionEntity;
import com.cpm.account.repository.AccountTransactionRepository;
import com.cpm.account.statics.TransactionType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AccountTransactionService {
    private final AccountTransactionRepository repository;

    public void create(Long sourceTargetId, Long targetAccountId, BigDecimal amount) {
        create(new AccountTransferRequest(sourceTargetId, targetAccountId, amount));
    }

    public void create(AccountTransferRequest model) {
        if (model.getTargetAccountId() == null && model.getSourceAccountId() == null) {
            throw new IllegalStateException("both source and target account can't be null");
        }
        if (model.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("transfer money can't be negative");
        }
        if (model.getSourceAccountId() != null) {
            AccountTransactionEntity transaction = AccountTransactionEntity.builder()
                    .amount(model.getAmount())
                    .accountId(model.getSourceAccountId())
                    .type(TransactionType.WITHDRAW)
                    .created(LocalDateTime.now())
                    .build();
            repository.save(transaction);
        }
        if (model.getTargetAccountId() != null) {
            AccountTransactionEntity transaction = AccountTransactionEntity.builder()
                    .amount(model.getAmount())
                    .accountId(model.getTargetAccountId())
                    .type(TransactionType.DEPOSIT)
                    .created(LocalDateTime.now())
                    .build();
            repository.save(transaction);
        }
    }
}
