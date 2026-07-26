package org.example;

class SleepThread extends Thread {

    private String threadName;
    private int sleepTime;

    SleepThread(String name, int time) {
        threadName = name;
        sleepTime = time;
    }

    @Override
    public void run() {

        for (int count = 1; count <= 3; count++) {

            System.out.println(
                    threadName + " is running: " + count
            );

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println(
                        threadName + " interrupted"
                );
            }
        }
    }
}

public class Day5_ThreadSleep {

    public static void main(String[] args) {

        SleepThread first =
                new SleepThread("T1", 2000);

        SleepThread second =
                new SleepThread("T2", 1000);

        SleepThread third =
                new SleepThread("T3", 0);

        first.start();
        second.start();
        third.start();
    }
}
