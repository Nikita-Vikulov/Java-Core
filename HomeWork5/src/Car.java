import java.util.concurrent.BrokenBarrierException;

public class Car implements Runnable {
    public static int CARS_COUNT;
    private Race race;
    private int speed;
    private String name;
    static boolean win = true;
    static boolean start = true;
    static boolean finish = true;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(100 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            MainClass.inception.await();
            if (start){
                start = false;
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

    }
}
