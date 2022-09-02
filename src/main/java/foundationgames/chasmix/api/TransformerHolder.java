package foundationgames.chasmix.api;

import org.quiltmc.chasm.api.Transformer;
import org.quiltmc.chasm.lang.api.ast.Node;

/**
 * The source for a Chasm transformer
 */
public interface TransformerHolder {
    /**
     * @return a Chasm transformer from this holder's source
     */
    Transformer toChasmTransformer();

    /**
     * @return the source Chassembly for this holder
     */
    Node asChassembly();

    /**
     * @return the parent path of this transformer (equivalent to the mixin package name, separated by '/')
     */
    String getPath();

    /**
     * @return what will be the ID of the finally produced Chasm transformer
     */
    String getId();
}
