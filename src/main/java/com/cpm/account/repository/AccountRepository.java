package com.cpm.account.repository;

import com.cpm.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity,Long> {

    @Query(value = "select coalesce(sum(at_deposit.amount),0) - coalesce(sum(at_withdraw.amount),0) from account a " +
            " left join account_transaction at_deposit on a.id_pk = at_deposit.account_id_fk and at_deposit.type = 'DEPOSIT'" +
            " left join account_transaction at_withdraw on a.id_pk = at_withdraw.account_id_fk and at_withdraw.type = 'WITHDRAW'" +
            " where a.id_pk = :id",nativeQuery = true)
    public BigDecimal getBalance(@Param("id") Long id);

}
