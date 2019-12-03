package payment.service;

import core.events.payment.MoneyTransferFailedEvent;
import core.events.payment.MoneyTransferredEvent;
import core.exception.AccountNotFound;
import core.exception.InsufficientBalance;
import core.models.TransferMoney;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import payment.entities.Account;
import payment.repository.AccountRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Value(value = "${kafka.event.topic}")
    private String eventTopic;

    @Qualifier("eventKafkaTemplate")
    @Autowired
    private KafkaTemplate<Long, Object> eventKafkaTemplate;

    @Transactional
    public synchronized void transferMoney(long from, long to, BigDecimal amount) throws InsufficientBalance,AccountNotFound {
        Optional<Account> fromAccountOptional = accountRepository.findById(from);
        if(fromAccountOptional.isPresent()){
           Account fromAccount =  fromAccountOptional.get();
           if(fromAccount.getBalance().compareTo(amount) >= 0){
               Optional<Account> toAccountOptional = accountRepository.findById(to);
               if(toAccountOptional.isPresent()){
                   Account toAccount =  toAccountOptional.get();
                   fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                   toAccount.setBalance(toAccount.getBalance().add(amount));
                   accountRepository.save(fromAccount);
                   accountRepository.save(toAccount);
                   accountRepository.flush();
               }
               else{
                   throw new AccountNotFound();
               }
           }
           else {
               throw new InsufficientBalance();
           }
        }
        else {
            throw new AccountNotFound();
        }
    }

    public void transferMoney(TransferMoney transferMoney) throws AccountNotFound, InsufficientBalance {
        try {
            transferMoney(transferMoney.getFrom(),transferMoney.getTo(),transferMoney.getAmount());
            MoneyTransferredEvent moneyTransferredEvent = new MoneyTransferredEvent();
            moneyTransferredEvent.setAmount(transferMoney.getAmount());
            moneyTransferredEvent.setFrom(transferMoney.getFrom());
            moneyTransferredEvent.setTo(transferMoney.getTo());
            moneyTransferredEvent.setOrderId(transferMoney.getOrderId());
            eventKafkaTemplate.send(eventTopic,moneyTransferredEvent.getOrderId(),moneyTransferredEvent);
            logger.info("MoneyTransferredEvent successfully published for order id: "+moneyTransferredEvent.getOrderId());
        } catch (InsufficientBalance | AccountNotFound exception) {
            MoneyTransferFailedEvent moneyTransferFailedEvent = new MoneyTransferFailedEvent();
            moneyTransferFailedEvent.setAmount(transferMoney.getAmount());
            moneyTransferFailedEvent.setFrom(transferMoney.getFrom());
            moneyTransferFailedEvent.setTo(transferMoney.getTo());
            moneyTransferFailedEvent.setOrderId(transferMoney.getOrderId());
            eventKafkaTemplate.send(eventTopic,moneyTransferFailedEvent.getOrderId(),moneyTransferFailedEvent);
            logger.error("MoneyTransferredEvent successfully published for order id: "+moneyTransferFailedEvent.getOrderId());
            throw exception;
        }
    }
}
