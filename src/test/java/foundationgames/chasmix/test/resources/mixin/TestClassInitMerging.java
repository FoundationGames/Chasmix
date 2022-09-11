package foundationgames.chasmix.test.resources.mixin;

import foundationgames.chasmix.test.resources.targets.FieldTestTarget;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FieldTestTarget.class)
public class TestClassInitMerging {
    public static final String TEST_STRING;

    static {
        /* TODO: Add this back when Chasm has solutions for merging labels
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            res.append((char) i);
        }
        TEST_STRING = res.toString();
         */

        TEST_STRING = "Hello" + "World";
        System.out.println(TEST_STRING);
    }
}
