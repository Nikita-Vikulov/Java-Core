public class Main {

    public static void main(String[] args) {
        Class cs = TestApp.class;
        try {
            TestClass.start(cs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
