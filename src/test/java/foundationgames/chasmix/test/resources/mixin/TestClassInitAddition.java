package foundationgames.chasmix.test.resources.mixin;

import foundationgames.chasmix.test.resources.targets.EmptyTestTarget;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EmptyTestTarget.class)
public class TestClassInitAddition {
    public static final char[] TEST_CHARS = {
            'f', 'o', 'o',
            'b', 'a', 'r'
    };

    static {
        StringBuilder res = new StringBuilder();
        for (char testChar : TEST_CHARS) {
            res.append(testChar);
        }

        System.out.println(res);
    }
}
