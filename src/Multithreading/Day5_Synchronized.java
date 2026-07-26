package org.example;

class Counter {

    private int value = 0;

    public synchronized void increment() {

        value++;

        System.out.println(
                Thread.currentThread().getName()
                        + " : " + value
        );
    }
}

class CounterThread extends Thread {

    private Counter counter;

    CounterThread(Counter counter, String name) {
        super(name);
        this.counter = counter;
    }

    @Override
    public void run() {

        for (int i = 1; i <= 5; i++) {
            counter.increment();
        }
    }
}

public class Day5_Synchronized {

    public static void main(String[] args)
            throws InterruptedException {

        Counter sharedCounter =
                new Counter();

        CounterThread first =
                new CounterThread(
                        sharedCounter,
                        "Thread-1"
                );

        CounterThread second =
                new CounterThread(
                        sharedCounter,
                        "Thread-2"
                );

        first.start();
        second.start();

        first.join();
        second.join();

        System.out.println(
                "Both threads completed"
        );
    }
}
