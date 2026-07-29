package com.bank;


import org.springframework.stereotype.Service;


   @Service
   public class EmailNotificationService implements NotificationService {
       @Override
       public void notifyCustomer(String message) {
           System.out.println("Email sent: " + message);
       }
}
