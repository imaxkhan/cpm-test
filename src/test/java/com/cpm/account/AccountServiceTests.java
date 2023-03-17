package com.cpm.account;

import com.cpm.account.dto.account.AccountBalanceResponse;
import com.cpm.account.dto.account.AccountRequest;
import com.cpm.account.dto.account.AccountResponse;
import com.cpm.account.dto.account.AccountTransferRequest;
import com.cpm.account.dto.user.UserResponse;
import com.cpm.account.service.AccountService;
import com.cpm.account.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class AccountServiceTests {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    @Test
    public void createAccount_user_exists_with_initial_credit() {
        BigDecimal initialCredit = BigDecimal.TEN;
        List<UserResponse> users = userService.findAll(0, 10);
        AccountRequest request = new AccountRequest();
        request.setInitialCredit(initialCredit);
        request.setCustomerId(users.get(0).getId());

        accountService.initAccount(request);

        UserResponse userResponse = userService.findById(users.get(0).getId());

        assertEquals(1, userResponse.getAccounts().size());
        assertEquals(0, initialCredit.compareTo(userResponse.getAccounts().get(0).getBalance()));
        Assertions.assertEquals(1, userResponse.getAccounts().get(0).getTransactions().size());
        assertEquals(0, initialCredit.compareTo(userResponse.getBalance()));

    }

    @Test
    public void createAccount_user_exists_with_negative_initial_credit() {
        BigDecimal initialCredit = BigDecimal.TEN.negate();
        List<UserResponse> users = userService.findAll(0, 10);
        AccountRequest request = new AccountRequest();
        request.setInitialCredit(initialCredit);
        request.setCustomerId(users.get(1).getId());

        try {
            accountService.initAccount(request);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("initial credit could not be negative", e.getMessage());
        }

    }

    @Test
    public void createAccount_user_exists_without_initial_credit() {
        BigDecimal initialCredit = BigDecimal.ZERO;
        List<UserResponse> users = userService.findAll(0, 10);
        AccountRequest request = new AccountRequest();
        request.setCustomerId(users.get(2).getId());

        accountService.initAccount(request);

        UserResponse userResponse = userService.findById(users.get(2).getId());

        assertEquals(1, userResponse.getAccounts().size());
        assertEquals(0, initialCredit.compareTo(userResponse.getAccounts().get(0).getBalance()));
        Assertions.assertEquals(0, userResponse.getAccounts().get(0).getTransactions().size());
        assertEquals(0, initialCredit.compareTo(userResponse.getBalance()));
    }

    @Test
    public void createAccount_user_not_exists_throw_exception() {
        AccountRequest request = new AccountRequest();
        request.setCustomerId(-1L);

        try {
            accountService.initAccount(request);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("user not exist", e.getMessage());
        }
    }

    @Test
    public void getBalance_return_balance() {
        BigDecimal initialCredit = BigDecimal.TEN;
        List<UserResponse> users = userService.findAll(0, 10);
        AccountRequest request = new AccountRequest();
        request.setInitialCredit(initialCredit);
        request.setCustomerId(users.get(0).getId());
        AccountResponse response = accountService.initAccount(request);

        AccountBalanceResponse accountBalance = accountService.getBalance(response.getId());

        assertEquals(0, accountBalance.getBalance().compareTo(initialCredit));
        assertEquals(accountBalance.getId(), response.getId());
    }

    @Test
    public void transferMoney_deposit_to_target_account() {
        BigDecimal initialCredit = BigDecimal.TEN;
        List<UserResponse> users = userService.findAll(0, 10);
        AccountRequest request = new AccountRequest();
        request.setInitialCredit(initialCredit);
        request.setCustomerId(users.get(0).getId());
        AccountResponse response = accountService.initAccount(request);

        BigDecimal transferAmount = BigDecimal.valueOf(100);
        AccountTransferRequest transferRequest = new AccountTransferRequest();
        transferRequest.setTargetAccountId(response.getId());
        transferRequest.setAmount(transferAmount);

        accountService.transferMoney(transferRequest);

        AccountBalanceResponse accountBalance = accountService.getBalance(response.getId());

        assertEquals(0, accountBalance.getBalance().compareTo(initialCredit.add(transferAmount)));
    }

    @Test
    public void transferMoney_withdraw_from_source_deposit_to_target_account() {
        List<UserResponse> users = userService.findAll(0, 10);

        BigDecimal initialCreditSource = BigDecimal.TEN;
        AccountRequest requestSource = new AccountRequest();
        requestSource.setInitialCredit(initialCreditSource);
        requestSource.setCustomerId(users.get(0).getId());
        AccountResponse responseSource = accountService.initAccount(requestSource);

        BigDecimal initialCreditTarget = BigDecimal.TEN;
        AccountRequest requestTarget = new AccountRequest();
        requestTarget.setInitialCredit(initialCreditTarget);
        requestTarget.setCustomerId(users.get(1).getId());
        AccountResponse responseTarget = accountService.initAccount(requestTarget);

        BigDecimal transferAmount = BigDecimal.valueOf(5);
        AccountTransferRequest transferRequest = new AccountTransferRequest();
        transferRequest.setTargetAccountId(responseTarget.getId());
        transferRequest.setSourceAccountId(responseSource.getId());
        transferRequest.setAmount(transferAmount);

        accountService.transferMoney(transferRequest);

        AccountBalanceResponse accountBalanceTarget = accountService.getBalance(responseTarget.getId());
        AccountBalanceResponse accountBalanceSource = accountService.getBalance(responseSource.getId());

        assertEquals(0, accountBalanceTarget.getBalance().compareTo(initialCreditTarget.add(transferAmount)));
        assertEquals(0, accountBalanceSource.getBalance().compareTo(initialCreditSource.subtract(transferAmount)));
    }

    @Test
    public void transferMoney_withdraw_from_source_deposit_to_target_account_with_lower_source_balance_than_transfer_amount() {
        List<UserResponse> users = userService.findAll(0, 10);

        BigDecimal initialCreditSource = BigDecimal.TEN;
        AccountRequest requestSource = new AccountRequest();
        requestSource.setInitialCredit(initialCreditSource);
        requestSource.setCustomerId(users.get(0).getId());
        AccountResponse responseSource = accountService.initAccount(requestSource);

        BigDecimal initialCreditTarget = BigDecimal.TEN;
        AccountRequest requestTarget = new AccountRequest();
        requestTarget.setInitialCredit(initialCreditTarget);
        requestTarget.setCustomerId(users.get(1).getId());
        AccountResponse responseTarget = accountService.initAccount(requestTarget);

        BigDecimal transferAmount = BigDecimal.valueOf(15);
        AccountTransferRequest transferRequest = new AccountTransferRequest();
        transferRequest.setTargetAccountId(responseTarget.getId());
        transferRequest.setSourceAccountId(responseSource.getId());
        transferRequest.setAmount(transferAmount);

        try {
            accountService.transferMoney(transferRequest);
            fail();
        }catch (IllegalArgumentException e){
            assertEquals("transfer amount is bigger that source account balance",e.getMessage());
        }
        AccountBalanceResponse accountBalanceTarget = accountService.getBalance(responseTarget.getId());
        AccountBalanceResponse accountBalanceSource = accountService.getBalance(responseSource.getId());

        assertEquals(0, accountBalanceTarget.getBalance().compareTo(initialCreditTarget));
        assertEquals(0, accountBalanceSource.getBalance().compareTo(initialCreditSource));
    }
}
