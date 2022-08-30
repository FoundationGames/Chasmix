package foundationgames.chasmix.test.resources.mixin;

import foundationgames.chasmix.test.resources.TestInterface;
import foundationgames.chasmix.test.resources.targets.TestTarget;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TestTarget.class)
public class TestInterfaceMerging implements TestInterface {
}
