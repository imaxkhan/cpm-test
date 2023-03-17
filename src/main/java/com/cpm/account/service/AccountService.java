package com.cpm.account.service;

import com.cpm.account.dto.account.AccountBalanceResponse;
import com.cpm.account.dto.account.AccountRequest;
import com.cpm.account.dto.account.AccountResponse;
import com.cpm.account.dto.account.AccountTransferRequest;
import com.cpm.account.entity.AccountEntity;
import com.cpm.account.entity.UserEntity;
import com.cpm.account.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository repository;
    private final UserService userService;
    private final AccountTransactionService accountTransactionService;


    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public AccountResponse initAccount(AccountRequest model) {
        Optional<UserEntity> user = userService.findEntityById(model.getCustomerId());
        if (user.isEmpty()) {
            throw new IllegalStateException("user not exist");
        }
        AccountEntity account = AccountEntity.builder()
                .created(LocalDateTime.now())
                .userId(model.getCustomerId())
                .build();

        repository.save(account);

        if (model.getInitialCredit() != null) {
            if (model.getInitialCredit().compareTo(BigDecimal.ZERO) > 0) {
                accountTransactionService.create(null, account.getId(), model.getInitialCredit());
            } else if (model.getInitialCredit().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalStateException("initial credit could not be negative");
            }
        }
        return new AccountResponse(account);
    }

    @Transactional(rollbackFor = Exception.class)
    public void transferMoney(AccountTransferRequest request) {
        if (request.getSourceAccountId() != null) {
            BigDecimal sourceBalance = repository.getBalance(request.getSourceAccountId());
            if (sourceBalance.compareTo(request.getAmount()) < 0) {
                throw new IllegalArgumentException("transfer amount is bigger that source account balance");
            }
        }
        accountTransactionService.create(request.getSourceAccountId(), request.getTargetAccountId(), request.getAmount());
    }

    public AccountBalanceResponse getBalance(Long id) {
        return AccountBalanceResponse
                .builder()
                .balance(repository.getBalance(id))
                .id(id)
                .build();
    }

}
