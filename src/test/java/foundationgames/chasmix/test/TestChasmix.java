package foundationgames.chasmix.test;

import foundationgames.chasmix.api.Chasmix;
import foundationgames.chasmix.api.ChasmixEnv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quiltmc.chasm.lang.internal.render.Renderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public final class TestChasmix {
    private static final Renderer RENDERER = Renderer.builder()
            .prettyPrinting(true)
            .indentation(4)
            .trailingCommas(false)
            .trailingNewline(false)
            .build();

    @Test
    public void testTransformerGeneration() throws IOException, ClassNotFoundException {
        var chasmix = Chasmix.create(new TestChasmixContext());

        for (var transformer : chasmix.generateTransformers(ChasmixEnv.DEFAULT)) {
            var tId = transformer.getId();
            var resultFileName = tId.substring(tId.lastIndexOf("/"));

            try (var resultFile = this.getClass().getClassLoader()
                    .getResourceAsStream("results/transformers/"+resultFileName+".chasm")) {
                var actualString = RENDERER.render(transformer.asChassembly());
                var expectedString = new BufferedReader(new InputStreamReader(resultFile))
                        .lines().collect(Collectors.joining("\n"));

                Assertions.assertEquals(expectedString, actualString);
            }
        }
    }

    // TODO: tests for transformer application
}
