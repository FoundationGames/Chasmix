package foundationgames.chasmix.test.resources.mixin;

import foundationgames.chasmix.test.resources.TestInterface;
import foundationgames.chasmix.test.resources.targets.EmptyTestTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(EmptyTestTarget.class)
public class TestPseudo implements TestInterface {
}
