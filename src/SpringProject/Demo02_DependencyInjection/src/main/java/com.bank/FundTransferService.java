package com.bank;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


   @Component
   public class FundTransferService {
       private final NotificationService notificationService;


   @Autowired
   public FundTransferService(NotificationService notificationService) {
       this.notificationService = notificationService;
   }


   public void transferFunds(String fromAcc, String toAcc, double amount) {
       System.out.println("Transferred " + amount + " from " + fromAcc + " to " + toAcc);
       notificationService.notifyCustomer("Transfer of " + amount + " completed.");
   }
}
