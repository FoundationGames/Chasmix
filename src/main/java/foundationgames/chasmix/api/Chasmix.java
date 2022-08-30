package foundationgames.chasmix.api;

import foundationgames.chasmix.ChasmixImpl;

import java.io.IOException;
import java.util.List;

/**
 * An instance of Chasmix.
 *
 * <p>Requires a {@link ChasmixContext}, which must be implemented by the API user.
 */
public interface Chasmix {
    /**
     * Create a new Chasmix instance.
     *
     * @param context a {@link ChasmixContext}, which specifies how Chasmix should discover Mixin configs and classes
     * @return a new Chasmix instance
     */
    static Chasmix create(ChasmixContext context) {
        return new ChasmixImpl(context);
    }

    /**
     * Generates all the transformers to be applied in the specified environment.
     *
     * @param env the environment for which transformers are needed - default (common), client, server
     * @return a list of {@link TransformerHolder}s, which can be turned into Chasm transformers or read as Chassembly nodes
     * @throws IOException if an IO error occured during class reading
     * @throws ClassNotFoundException if a class specified by a mixin could not be found
     */
    List<TransformerHolder> generateTransformers(ChasmixEnv env) throws IOException, ClassNotFoundException;
}
