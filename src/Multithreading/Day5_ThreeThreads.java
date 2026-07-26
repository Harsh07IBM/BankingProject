package org.example;

class MyThread extends Thread {

    private String threadName;

    MyThread(String name) {
        threadName = name;
    }

    @Override
    public void run() {

        for (int i = 1; i <= 5; i++) {
            System.out.println(threadName + " is running: " + i);
        }
    }
}

public class Day5_ThreeThreads {

    public static void main(String[] args) {

        MyThread first = new MyThread("T1");
        MyThread second = new MyThread("T2");
        MyThread third = new MyThread("T3");

        first.start();
        second.start();
        third.start();
    }
}
