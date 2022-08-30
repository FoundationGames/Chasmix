package foundationgames.chasmix.chasm;

import foundationgames.chasmix.api.TransformerHolder;
import org.quiltmc.chasm.api.Transformer;
import org.quiltmc.chasm.internal.transformer.ChasmLangTransformer;
import org.quiltmc.chasm.lang.api.ast.Node;

public class TransformerHolderImpl implements TransformerHolder {
    private final String id;
    private final Node node;

    public TransformerHolderImpl(String id, Node node) {
        this.id = id;
        this.node = node;
    }

    @Override
    public Transformer toChasmTransformer() {
        return new ChasmLangTransformer(this.id, this.node);
    }

    @Override
    public Node asChassembly() {
        return this.node;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
