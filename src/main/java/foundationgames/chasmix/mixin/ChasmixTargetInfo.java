package foundationgames.chasmix.mixin;

import foundationgames.chasmix.api.ChasmixContext;
import foundationgames.chasmix.mixin.throwables.ChasmixProcessError;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;

public class ChasmixTargetInfo {
    public final String name;
    private final ClassNode classNode;

    public ChasmixTargetInfo(ChasmixContext context, String name) throws IOException {
        this.name = name;

        ClassNode node = null;
        try {
            node = context.getClassNode(name);
        } catch (ClassNotFoundException ignored) {}
        this.classNode = node;
    }

    public Optional<ClassNode> getClassNode() {
        return Optional.ofNullable(this.classNode);
    }

    /**
     * Expect that the target class is a certain way, in order to provide detailed errors to users during compile
     * <p>If the target class is not found in the user's environment, this method does nothing
     *
     * @param expectation a statement that must be true about the class
     * @param error an error message to report, if the above expectation does not hold true
     */
    public void expect(Predicate<ClassNode> expectation, String error) {
        var classNode = getClassNode();
        if (classNode.isPresent() && !expectation.test(classNode.get())) {
            throw new ChasmixProcessError(error);
        }
    }
}
