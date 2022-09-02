package foundationgames.chasmix.chasm;

import foundationgames.chasmix.api.TransformerHolder;
import org.quiltmc.chasm.api.Transformer;
import org.quiltmc.chasm.internal.transformer.ChasmLangTransformer;
import org.quiltmc.chasm.lang.api.ast.Node;

public class TransformerHolderImpl implements TransformerHolder {
    private final String path;
    private final String name;
    private final Node node;

    public TransformerHolderImpl(String path, String name, Node node) {
        this.path = path;
        this.name = name;
        this.node = node;
    }

    @Override
    public Transformer toChasmTransformer() {
        return new ChasmLangTransformer(this.getId(), this.node);
    }

    @Override
    public Node asChassembly() {
        return this.node;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getId() {
        return this.path + "/" + this.name;
    }
}
