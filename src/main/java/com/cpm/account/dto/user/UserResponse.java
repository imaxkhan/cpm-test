package com.cpm.account.dto.user;

import com.cpm.account.dto.account.AccountResponse;
import com.cpm.account.entity.AccountEntity;
import com.cpm.account.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class UserResponse {
    private long id;
    private String name;
    private String surname;
    private BigDecimal balance;
    private List<AccountResponse> accounts;

    public UserResponse(UserEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.surname = entity.getSurname();
        if (Hibernate.isInitialized(entity.getAccounts())) {
            this.accounts = new ArrayList<>();
            this.balance = BigDecimal.ZERO;
            for (AccountEntity eachAccount : entity.getAccounts()) {
                AccountResponse account = new AccountResponse(eachAccount);
                this.balance = this.balance.add(account.getBalance());
                this.accounts.add(account);
            }
        }
    }
}
