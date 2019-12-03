package payment;

import core.commands.payment.CreateAccountCommand;
import core.models.Address;
import core.models.PaymentAccount;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import payment.config.PaymentKafkaConfig;
import payment.config.PaymentSQLConfig;
import payment.repository.AccountRepository;

import java.math.BigDecimal;


public class PaymentApp {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(PaymentKafkaConfig.class, PaymentSQLConfig.class);
        AccountRepository accountRepository = ctx.getBean(AccountRepository.class);
        Thread.sleep(1000000);
    }

    public void publishCreateAccountCommand() {

        Address address = new Address();
        address.setAddress("DSR Spring beauty Apt,Brookfield");
        address.setCity("Bangalore");
        address.setCountry("India");
        address.setPhone("1234");
        address.setState("Karnataka");
        address.setZip("560037");
        PaymentAccount paymentAccount = new PaymentAccount();
        paymentAccount.setAddress(address);
        paymentAccount.setEmail("sam@gmail.com");
        paymentAccount.setName("sameer");
        paymentAccount.setPhone("214");


        address = new Address();
        address.setAddress("Moriz Restaurant,Brookfield");
        address.setCity("Bangalore");
        address.setCountry("India");
        address.setPhone("458878");
        address.setState("Karnataka");
        address.setZip("560037");
        paymentAccount = new PaymentAccount();
        paymentAccount.setAddress(address);
        paymentAccount.setEmail("moriz.restaurant@gmail.com");
        paymentAccount.setName("Moriz Restaurant");
        paymentAccount.setPhone("4363");
    }
}
