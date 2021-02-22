import java.util.concurrent.CyclicBarrier;

public class MainClass {

    public static final int CARS_COUNT = 4;
    public static int count = 0;
    static CyclicBarrier inception = new CyclicBarrier(4);
    static CyclicBarrier road = new CyclicBarrier(4);
    static CyclicBarrier tunnel = new CyclicBarrier(2);
    static int tunnelWait = 0;

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        cars[0] = new Car(race, 20 + (int) (Math.random() * 10));
        cars[1] = new Car(race, 20 + (int) (Math.random() * 10));
        cars[2] = new Car(race, 20 + (int) (Math.random() * 10));
        cars[3] = new Car(race, 20 + (int) (Math.random() * 10));

        for (Car car : cars) {
            new Thread(car).start();
        }
    }
}

