package foundationgames.chasmix.test;

import foundationgames.chasmix.ChasmixImpl;
import foundationgames.chasmix.api.ChasmixConfig;
import foundationgames.chasmix.api.ChasmixContext;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class TestChasmixContext implements ChasmixContext {
    private final ClassLoader classLoader = this.getClass().getClassLoader();

    @Override
    public List<ChasmixConfig> getAllMixinConfigs() {
        try (var in = this.classLoader.getResourceAsStream("test_chasmix.mixins.json")) {
            if (in != null) {
                return List.of(ChasmixConfig.read(in));
            }
        } catch (IOException e) {
            ChasmixImpl.LOG.log(Level.WARNING, e.getMessage());
        }

        return List.of();
    }

    @Override
    public ClassNode getClassNode(String name) throws ClassNotFoundException, IOException {
        try (var in = this.classLoader.getResourceAsStream(name + ".class")) {
            if (in == null) {
                throw new ClassNotFoundException(name);
            }

            var ret = new ClassNode();
            new ClassReader(in.readAllBytes()).accept(ret, 0);
            return ret;
        }
    }
}
