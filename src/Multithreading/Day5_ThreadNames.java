package org.example;

class TimeThread extends Thread {

    private String periodName;
    private int delay;

    TimeThread(String name, int sleepTime) {
        periodName = name;
        delay = sleepTime;
    }

    @Override
    public void run() {

        for (int i = 1; i <= 3; i++) {

            System.out.println(
                    periodName + " Thread: " + i
            );

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                System.out.println(
                        periodName + " interrupted"
                );
            }
        }
    }
}

public class Day5_ThreadNames {

    public static void main(String[] args) {

        TimeThread morning =
                new TimeThread("Morning", 2000);

        TimeThread afternoon =
                new TimeThread("Afternoon", 1000);

        TimeThread evening =
                new TimeThread("Evening", 0);

        morning.start();
        afternoon.start();
        evening.start();
    }
}
