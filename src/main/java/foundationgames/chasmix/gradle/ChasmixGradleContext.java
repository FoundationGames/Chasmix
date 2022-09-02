package foundationgames.chasmix.gradle;

import foundationgames.chasmix.api.ChasmixConfig;
import foundationgames.chasmix.api.ChasmixContext;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ChasmixGradleContext implements ChasmixContext {
    private final Project gradleProject;
    private final List<ChasmixConfig> configs;
    private final FileCollection jarSource;

    public ChasmixGradleContext(Project project, List<ChasmixConfig> configs, FileCollection jarSource) {
        this.gradleProject = project;
        this.configs = configs;
        this.jarSource = jarSource;
    }

    @Override
    public List<ChasmixConfig> getAllMixinConfigs() {
        return this.configs;
    }

    @Override
    public ClassNode getClassNode(String name) throws ClassNotFoundException, IOException {
        var fileName = name + ".class";

        try {
            var bytes = Files.readAllBytes(
                    jarSource.filter(file ->
                            fileName.equals(fixClassFileName(file.getAbsolutePath()))).getSingleFile().toPath());

            var ret = new ClassNode();
            new ClassReader(bytes).accept(ret, 0);
            return ret;
        } catch (IllegalStateException ignored) {
        }

        throw new ClassNotFoundException(name);
    }

    private String fixClassFileName(String name) {
        name = name
                .replaceAll("\\\\", "/")
                .replaceFirst(this.gradleProject.getBuildDir().getAbsolutePath()
                        .replaceAll("\\\\", "/"), "")
                .replaceFirst("/classes/java/", "");
        return name.substring(name.indexOf("/") + 1); // Eliminate source set name
    }
}
