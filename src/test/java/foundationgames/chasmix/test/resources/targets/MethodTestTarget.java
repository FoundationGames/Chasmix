package foundationgames.chasmix.test.resources.targets;

public class MethodTestTarget {
    public static final String CONSTANT = "A Constant String";

    public static int staticMethod(int[] array) {
        int result = 0;
        for (int x : array) {
            result += x;
        }

        return result;
    }

    public void instanceMethod() {
        System.out.println("Hello from Instance");
    }

    private void privateInstanceMethod(Object value) {
        System.out.println("Private Hello from Instance: " + value);
    }

    static {
        for (int i = 0; i < 5; i++) {
            System.out.println(CONSTANT);
        }
    }
}
