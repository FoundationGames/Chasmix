package foundationgames.chasmix.test.resources.mixin;

import foundationgames.chasmix.test.resources.TestDuckInterface;
import foundationgames.chasmix.test.resources.targets.MethodTestTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MethodTestTarget.class)
public abstract class TestMethodMerging implements TestDuckInterface {
    @Shadow protected abstract void privateInstanceMethod(Object value);

    private void newMethod() {
        this.privateInstanceMethod("Foo");
        newStaticMethod();

        ((MethodTestTarget) (Object) this).instanceMethod();
    }

    private static void newStaticMethod() {
        System.out.println("New Static Method");
    }

    @Override
    public void quack(boolean loudly) {
        System.out.println("Quack");

        if (loudly) {
            this.newMethod();
        }
    }
}
