class StaticVariableDemo {
    static int a = 10;
    int b = 10;

    void increment() {
        a++;
        b++;
        System.out.println("Static = " + a);
        System.out.println("Normal = " + b);
    }

    public static void main(String[] args) {
        StaticVariableDemo d1 = new StaticVariableDemo();
        StaticVariableDemo d2 = new StaticVariableDemo();

        d1.increment();
        d2.increment();
    }
}
