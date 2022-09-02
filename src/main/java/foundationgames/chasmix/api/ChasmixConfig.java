package foundationgames.chasmix.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import foundationgames.chasmix.mixin.ChasmixConfigImpl;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Represents a Mixin config.
 */
public interface ChasmixConfig {
    /**
     * Creates a Chasmix Mixin config from an input stream.
     *
     * @param jsonIn an {@link InputStream} containing the Mixin config as JSON
     * @return a {@link ChasmixConfig} constructed from the JSON provided
     */
    static ChasmixConfig read(InputStream jsonIn) throws JsonSyntaxException {
        return new Gson().fromJson(new InputStreamReader(jsonIn), ChasmixConfigImpl.class);
    }

    /**
     * @return this config's mixin package
     */
    String getPackage();

    /**
     * Gets the Mixin class names specified for the provided environment
     *
     * @param env the environment for which mixins are requested
     * @return a list of mixin class names (relative to this config's package) with packages separated by "."
     */
    String[] getMixins(ChasmixEnv env);
}
