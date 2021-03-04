import java.lang.reflect.Method;
import java.util.ArrayList;

public class TestClass {
    int bs = 0;
    int as = 0;

    public static void start(Class obj) throws Exception {
        TestClass tc = new TestClass();
        Method[] methods = obj.getDeclaredMethods();
        ArrayList<Method> arr = new ArrayList<>();
        for (Method m : methods) {
            if (m.isAnnotationPresent(BeforeSuite.class)) {
                tc.bs++;
            }
            if (m.isAnnotationPresent(AfterSuite.class)) {
                tc.as++;
            }
        }
        if ((tc.as | tc.bs) > 1) throw new RuntimeException();

        for (Method m : methods) {

            if (m.isAnnotationPresent(BeforeSuite.class)) {
                m.invoke(null);
            }

            if (m.isAnnotationPresent(Test.class)) {
                arr.add(m);
            }
        }

        for (int i = arr.size() - 1; i >= 0; i--) {
            System.out.print("Приоритет: " + arr.get(i).getAnnotation(Test.class).priority() + " Тест: ");
            arr.get(i).invoke(null);

        }

        for (Method m : methods) {
            if (m.isAnnotationPresent(AfterSuite.class)) {
                m.invoke(null);
            }
        }
    }
}


