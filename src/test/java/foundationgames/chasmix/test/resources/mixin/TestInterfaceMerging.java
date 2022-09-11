package foundationgames.chasmix.test.resources.mixin;

import foundationgames.chasmix.test.resources.TestInterface;
import foundationgames.chasmix.test.resources.targets.EmptyTestTarget;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EmptyTestTarget.class)
public class TestInterfaceMerging implements TestInterface {
}
