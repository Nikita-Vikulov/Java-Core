import java.util.concurrent.BrokenBarrierException;

public class Tunnel extends Stage {
    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }

    @Override
    public void go(Car c) {
        try {
            System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
            MainClass.tunnel.await();
            while (true) {
                MainClass.tunnelWait++;
                if (MainClass.tunnelWait < 3) {
                    System.out.println(c.getName() + " начал этап: " + description);
                    Thread.sleep(length / c.getSpeed() * 1000L);
                    // MainClass.tunnel.await();
                    System.out.println(c.getName() + " закончил этап: " + description);
                    MainClass.tunnelWait = 0;
                    return;
                } else {
                    Thread.sleep(3000);
                    System.out.println(c.getName() + " начал этап: " + description);
                    Thread.sleep(length / c.getSpeed() * 1000L);
                    // MainClass.tunnel.await();
                    System.out.println(c.getName() + " закончил этап: " + description);
                    MainClass.tunnelWait = 0;
                    return;
                }
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
