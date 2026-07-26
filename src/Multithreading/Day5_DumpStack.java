package org.example;

class LoopWorker extends Thread {

    LoopWorker(String name) {
        super(name);
    }

    @Override
    public void run() {

        for (int number = 1; number <= 20; number++) {

            System.out.println(
                    getName() + " : " + number
            );

            if (number == 10) {
                System.out.println(
                        "Printing Stack Trace:"
                );

                Thread.dumpStack();
            }
        }
    }
}

public class Day5_DumpStack {

    public static void main(String[] args) {

        LoopWorker worker =
                new LoopWorker("Loop-Thread");

        worker.start();
    }
}
