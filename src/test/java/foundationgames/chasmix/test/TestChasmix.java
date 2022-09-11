package foundationgames.chasmix.test;

import foundationgames.chasmix.api.Chasmix;
import foundationgames.chasmix.api.ChasmixContext;
import foundationgames.chasmix.api.ChasmixEnv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;
import org.quiltmc.chasm.api.ChasmProcessor;
import org.quiltmc.chasm.api.ClassData;
import org.quiltmc.chasm.api.util.ClassLoaderContext;
import org.quiltmc.chasm.lang.internal.render.Renderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TestChasmix {
    private static final Renderer RENDERER = Renderer.builder()
            .prettyPrinting(true)
            .indentation(4)
            .trailingCommas(false)
            .trailingNewline(false)
            .build();

    private static final String[] TARGET_CLASSES = {
            "foundationgames/chasmix/test/resources/targets/EmptyTestTarget",
            "foundationgames/chasmix/test/resources/targets/FieldTestTarget",
            "foundationgames/chasmix/test/resources/targets/MethodTestTarget"
    };

    @TestFactory
    public Stream<DynamicTest> createChasmixTests() throws IOException, ClassNotFoundException {
        var context = new TestChasmixContext();
        var chasmix = Chasmix.create(context);
        var transformers = chasmix.generateTransformers(ChasmixEnv.DEFAULT);

        var applicationTests = transformers.stream().map(holder ->
                DynamicTest.dynamicTest(
                        "Apply "+holder.getId()
                                .replaceFirst(holder.getPath()+"/", ""),
                        () -> {
                            var chasmContext = new ClassLoaderContext(null, this.getClass().getClassLoader());
                            var chasm = new ChasmProcessor(chasmContext);

                            for (var cfg : context.getAllMixinConfigs()) for (var mxn : cfg.getMixins(ChasmixEnv.DEFAULT)) {
                                addClass(chasm, context,
                                        cfg.getPackage().replaceAll("\\.", "/") + "/"
                                                + mxn.replaceAll("\\.", "/"));
                            }
                            for (var cls : TARGET_CLASSES) {
                                addClass(chasm, context, cls);
                            }

                            chasm.addTransformer(holder.toChasmTransformer(chasmContext));

                            var tId = holder.getId();
                            var resultFileName = tId.substring(tId.lastIndexOf("/"));

                            var processed = chasm.process(true);
                            Assertions.assertEquals(processed.size(), 1);

                            try (var resultFile = this.getClass().getClassLoader()
                                    .getResourceAsStream("results/classes/"+resultFileName+".result")) {
                                var actualString = new StringWriter();
                                var visitor = new TraceClassVisitor(new PrintWriter(actualString) {
                                    @Override public void println() {
                                        this.write("\n");
                                    }
                                });
                                var actualClass = new ClassReader(processed.get(0).getClassBytes());
                                actualClass.accept(visitor, 0);

                                var expectedString = new BufferedReader(new InputStreamReader(resultFile))
                                        .lines().collect(Collectors.joining("\n")) + "\n";

                                Assertions.assertEquals(expectedString, actualString.toString());
                            }
                        }));

        var transformerTests = transformers.stream().map(holder ->
                DynamicTest.dynamicTest(
                        "Verify Chassembly "+holder.getId()
                                .replaceFirst(holder.getPath()+"/", ""),
                        () -> {
                    var tId = holder.getId();
                    var resultFileName = tId.substring(tId.lastIndexOf("/"));

                    try (var resultFile = this.getClass().getClassLoader()
                            .getResourceAsStream("results/transformers/"+resultFileName+".chasm")) {
                        var actualString = RENDERER.render(holder.asChassembly());
                        var expectedString = new BufferedReader(new InputStreamReader(resultFile))
                                .lines().collect(Collectors.joining("\n"));

                        Assertions.assertEquals(expectedString, actualString);
                    }
                }));

        return Stream.concat(applicationTests, transformerTests);
    }

    private static void addClass(ChasmProcessor chasm, ChasmixContext ctx, String name) throws IOException, ClassNotFoundException {
        var cls = ctx.getClassNode(name);
        var wr = new ClassWriter(0);
        cls.accept(wr);

        chasm.addClass(new ClassData(wr.toByteArray()));
    }

    // TODO: tests for transformer application
}
