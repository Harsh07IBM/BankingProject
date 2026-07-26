package org.example;

class StatusThread extends Thread {

    @Override
    public void run() {

        System.out.println(
                "Thread is currently running"
        );

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }

        System.out.println(
                "Thread execution completed"
        );
    }
}

public class Day5_IsAlive {

    public static void main(String[] args)
            throws InterruptedException {

        StatusThread worker =
                new StatusThread();

        System.out.println(
                "Before start: " + worker.isAlive()
        );

        worker.start();

        System.out.println(
                "After start: " + worker.isAlive()
        );

        worker.join();

        System.out.println(
                "After completion: " + worker.isAlive()
        );
    }
}
