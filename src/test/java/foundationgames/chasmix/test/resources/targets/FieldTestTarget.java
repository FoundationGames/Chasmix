package foundationgames.chasmix.test.resources.targets;

public class FieldTestTarget {
    public static String staticField = "Hello World";

    private int field;
    private final int finalField = 2;

    public FieldTestTarget(int fieldValue) {
        this.field = fieldValue;
    }
}
