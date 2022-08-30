package foundationgames.chasmix;

import foundationgames.chasmix.api.Chasmix;
import foundationgames.chasmix.api.ChasmixContext;
import foundationgames.chasmix.api.ChasmixEnv;
import foundationgames.chasmix.api.TransformerHolder;
import foundationgames.chasmix.mixin.ChasmixMixinInfo;
import foundationgames.chasmix.mixin.TransformerGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChasmixImpl implements Chasmix {
    public static final Logger LOG = Logger.getLogger("Chasmix");

    private final ChasmixContext context;

    public ChasmixImpl(ChasmixContext context) {
        this.context = context;
    }

    @Override
    public List<TransformerHolder> generateTransformers(ChasmixEnv env) throws IOException, ClassNotFoundException {
        var transformers = new ArrayList<TransformerHolder>();

        for (var config : this.context.getAllMixinConfigs()) {
            var packageName = config.getPackage().replaceAll("\\.", "/");

            for (var mixinName : config.getMixins(env)) {
                mixinName = mixinName.replaceAll("\\.", "/");

                var mixinInfo = ChasmixMixinInfo.create(this.context.getClassNode(
                        packageName + "/" + mixinName));

                for (var targetName : mixinInfo.targets) {
                    transformers.add(
                            new TransformerGenerator(mixinInfo, this.context.getClassNode(targetName)).generate());
                }
            }
        }

        return transformers;
    }
}
