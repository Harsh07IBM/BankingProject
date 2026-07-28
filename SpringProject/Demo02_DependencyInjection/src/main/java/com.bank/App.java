package com.bank;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class App {
   public static void main(String[] args) {
       // Load Spring configuration from applicationContext.xml
       ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");


       // Get FundTransferService bean
       FundTransferService service = context.getBean(FundTransferService.class);


       // Perform a demo transfer
       service.transferFunds("ACC123", "ACC456", 5000.0);
   }
}
