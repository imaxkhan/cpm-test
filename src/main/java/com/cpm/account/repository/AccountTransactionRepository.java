package com.cpm.account.repository;

import com.cpm.account.entity.AccountTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransactionEntity, Long> {
}
