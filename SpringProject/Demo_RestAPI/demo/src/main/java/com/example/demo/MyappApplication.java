package com.example.myapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyappApplication {

    public static void main(String[] args) {
		for(int i=0;i<args.length;i++) {
			System.out.println("Args Params: "+args[i]);
		}
		// System.out.println("Args length: "+args.length);
		// System.out.println("Args Params: "+args[args.length-1]);
        SpringApplication.run(MyappApplication.class, args);
    }
}