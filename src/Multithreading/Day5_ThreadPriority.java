package org.example;

class PriorityWorker extends Thread {

    PriorityWorker(String name) {
        super(name);
    }

    @Override
    public void run() {

        for (int i = 1; i <= 5; i++) {

            System.out.println(
                    getName() + " is running: " + i
            );
        }
    }
}

public class Day5_ThreadPriority {

    public static void main(String[] args) {

        PriorityWorker morning =
                new PriorityWorker("Morning");

        PriorityWorker afternoon =
                new PriorityWorker("Afternoon");

        PriorityWorker evening =
                new PriorityWorker("Evening");

        morning.setPriority(Thread.MAX_PRIORITY);
        afternoon.setPriority(Thread.NORM_PRIORITY);
        evening.setPriority(Thread.MIN_PRIORITY);

        morning.start();
        afternoon.start();
        evening.start();
    }
}
