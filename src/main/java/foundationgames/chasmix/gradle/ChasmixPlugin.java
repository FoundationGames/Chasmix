package foundationgames.chasmix.gradle;

import com.google.gson.JsonSyntaxException;
import foundationgames.chasmix.api.Chasmix;
import foundationgames.chasmix.api.ChasmixConfig;
import foundationgames.chasmix.api.ChasmixEnv;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.jvm.tasks.Jar;
import org.quiltmc.chasm.lang.internal.render.Renderer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class ChasmixPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getTasks().withType(Jar.class, task -> ChasmixPlugin.generateChasmTransformers(project, task));
    }

    private static void generateChasmTransformers(Project project, Jar task) {
        var mixinConfigs = new ArrayList<ChasmixConfig>();
        for (var file : project.fileTree("src").filter(f -> f.getName().endsWith(".mixins.json"))) {
            try (var in = new FileInputStream(file)) {
                mixinConfigs.add(ChasmixConfig.read(in));
            } catch (IOException | JsonSyntaxException ex) {
                System.err.println(ex);
            }
        }

        var chasmix = Chasmix.create(new ChasmixGradleContext(project, mixinConfigs, task.getSource()));
        var renderer = Renderer.builder().trailingCommas(false).build();

        // Gradle may have a better solution for this instead of manual IO
        var builtChasmDir = project.getBuildDir().toPath().resolve("chasm");
        try {
            for (var holder : chasmix.generateTransformers(ChasmixEnv.DEFAULT)) {
                var dest = builtChasmDir.resolve(holder.getId() + ".chasm");
                Files.createDirectories(dest.getParent());
                Files.writeString(dest, renderer.render(holder.asChassembly()));

                task.from(dest, copy ->
                        copy.into("org/quiltmc/chasm/transformers/"+holder.getPath()));
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println(ex);
        }
    }
}
