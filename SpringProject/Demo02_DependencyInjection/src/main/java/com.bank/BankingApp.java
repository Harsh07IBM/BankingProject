package com.bank;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class BankingApp {
   public static void main(String[] args) {
       ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
       FundTransferService service = context.getBean(FundTransferService.class);
       service.transferFunds("ACC123", "ACC456", 5000.0);
   }
}
