package com.cpm.account;


import com.cpm.account.dto.account.AccountTransferRequest;
import com.cpm.account.service.AccountTransactionService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SpringBootTest
public class AccountTransactionServiceTest {

    @Autowired
    private AccountTransactionService accountTransactionService;

    @Test
    public void create_transfer_without_any_accountId(){
        AccountTransferRequest model = new AccountTransferRequest();
        model.setAmount(BigDecimal.TEN);

        try {
            accountTransactionService.create(model);
            fail();
        }catch (IllegalStateException e){
            assertEquals("both source and target account can't be null",e.getMessage());
        }
    }
    @Test
    public void create_transfer_with_negative_amount(){
        AccountTransferRequest model = new AccountTransferRequest();
        model.setAmount(BigDecimal.TEN.negate());
        model.setSourceAccountId(10L);

        try {
            accountTransactionService.create(model);
            fail();
        }catch (IllegalArgumentException e){
            assertEquals("transfer money can't be negative",e.getMessage());
        }
    }


}
