import java.util.concurrent.BrokenBarrierException;

public class Road extends Stage {
    public Road(int length) {
        this.length = length;
        this.description = "Дорога " + length + " метров";
    }

    @Override
    public void go(Car c) {
        try {
            System.out.println(c.getName() + " начал этап: " + description);
            Thread.sleep(length / c.getSpeed() * 1000L);
            System.out.println(c.getName() + " закончил этап: " + description);
            if(length == 40 && Car.win) {
                Car.win = false;
                System.out.println(c.getName() + " WIN");
            }
            MainClass.road.await();
            if(length == 40 && Car.finish) {
                Car.finish = false;
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();

        }
    }
}
