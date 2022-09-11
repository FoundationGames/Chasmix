package foundationgames.chasmix.test.resources.mixin;

import foundationgames.chasmix.test.resources.targets.FieldTestTarget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FieldTestTarget.class)
public class TestFieldMerging {
    private boolean newField;

    @Shadow
    private int field;

    @Shadow @Final @Mutable
    private int finalField;
}
