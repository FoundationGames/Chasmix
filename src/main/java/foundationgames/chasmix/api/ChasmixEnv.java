package foundationgames.chasmix.api;

/**
 * An environment or game distribution in which mixins may run
 */
public enum ChasmixEnv {
    DEFAULT("mixins"), CLIENT("client"), SERVER("server");

    public final String name;

    ChasmixEnv(String name) {
        this.name = name;
    }
}
