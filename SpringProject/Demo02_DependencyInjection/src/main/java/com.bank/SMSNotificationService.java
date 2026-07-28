package com.bank;


public class SMSNotificationService implements NotificationService {
   @Override
   public void notifyCustomer(String message) {
       System.out.println("SMS sent: " + message);
   }
}
