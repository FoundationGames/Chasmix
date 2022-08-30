package foundationgames.chasmix.mixin;

import com.google.gson.annotations.SerializedName;
import foundationgames.chasmix.api.ChasmixConfig;
import foundationgames.chasmix.api.ChasmixEnv;

/**
 * Represents a Mixin config (mixins.json)
 */
public class ChasmixConfigImpl implements ChasmixConfig {
    @SerializedName("package")
    private String mixinPackage;

    private String[] mixins;

    @SerializedName("client")
    private String[] clientMixins;

    @SerializedName("server")
    private String[] serverMixins;

    @Override
    public String getPackage() {
        return this.mixinPackage;
    }

    @Override
    public String[] getMixins(ChasmixEnv env) {
        return switch (env) {
            case CLIENT -> this.clientMixins;
            case SERVER -> this.serverMixins;
            default -> this.mixins;
        };
    }
}
