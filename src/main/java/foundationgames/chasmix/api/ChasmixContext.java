package foundationgames.chasmix.api;

import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.List;

/**
 * Tells a {@link Chasmix} instance how to access things like Mixin configs and classes
 */
public interface ChasmixContext {
    /**
     * @return all mixin configs currently present
     */
    List<ChasmixConfig> getAllMixinConfigs();

    /**
     * @param name the class name with package, separated by "/" - EX: {@code java/lang/Object}
     */
    ClassNode getClassNode(String name) throws ClassNotFoundException, IOException;
}
