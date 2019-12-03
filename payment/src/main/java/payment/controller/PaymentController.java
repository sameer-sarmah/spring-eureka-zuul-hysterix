package payment.controller;


import core.exception.AccountNotFound;
import core.exception.CoreException;
import core.exception.InsufficientBalance;
import core.models.Money;
import core.models.PaymentAccount;
import core.models.TransferMoney;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import payment.entities.Account;
import payment.repository.AccountRepository;
import payment.service.PaymentService;
import payment.util.PaymentUtil;

import java.util.Optional;

@RestController()
public class PaymentController {


    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AccountRepository accountRepository;

//    @PostMapping(value="payment")
//    public Account createAccount(@RequestBody PaymentAccount paymentAccount){
//        Account account = new Account();
//        account.setAddress(PaymentUtil.convertAddressModelToAddressEntity(paymentAccount.getAddress()));
//        account.setName(paymentAccount.getName());
//        account.setEmail(paymentAccount.getEmail());
//        Account savedAccount = accountRepository.save(account);
//        return savedAccount;
//    }


    @PutMapping(value = "payment/{accountId}")
    public Account addMoneyToAccount(@PathVariable Long accountId, @RequestBody Money money) throws AccountNotFound {

        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if(accountOptional.isPresent()){
            Account account = accountOptional.get();
            account.setBalance(money.getAmount());
            return accountRepository.saveAndFlush(account);
        }
        else{
            throw new AccountNotFound();
        }
    }

    @PostMapping(value = "payment/transfer-money")
    public void transferMoney(@RequestBody TransferMoney transferMoney) throws CoreException {
        try {
            paymentService.transferMoney(transferMoney);

        } catch (InsufficientBalance | AccountNotFound exception) {
            throw new CoreException("Transfer failed",exception);
        }
    }
}
