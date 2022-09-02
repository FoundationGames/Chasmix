package foundationgames.chasmix.mixin;

import foundationgames.chasmix.api.TransformerHolder;
import foundationgames.chasmix.chasm.Chassembler;
import foundationgames.chasmix.chasm.TransformerHolderImpl;
import org.objectweb.asm.tree.ClassNode;
import org.quiltmc.chasm.lang.api.ast.Node;

public class TransformerGenerator {
    public final ChasmixMixinInfo mixin;
    public final ClassNode target;

    private final Chassembler chasm = Chassembler.ofMap();
    private final Chassembler transformations = Chassembler.ofList();

    public TransformerGenerator(ChasmixMixinInfo mixin, ClassNode target) {
        this.mixin = mixin;
        this.target = target;
    }

    public TransformerHolder generate() {
        chasm.putClass("mxn", this.mixin.name.replaceAll("\\.", "/"));
        chasm.putClass("trg", this.target.name.replaceAll("\\.", "/"));

        // Interfaces
        if (this.mixin.interfaces.size() > 0) {
            this.mergeInterfaces();
        }

        chasm.put("transformations", transformations.asNode());

        var name = this.mixin.name;
        int split = name.lastIndexOf("/");
        return new TransformerHolderImpl(
                name.substring(0, split < 0 ? name.length() : split),
                name.substring(split + 1),
                this.chasm.asNode());
    }

    public void mergeInterfaces() {
        transformations.addTransform(
                Node.parse("trg.interfaces"),
                Node.parse("args -> mxn.interfaces[mxni -> len(args.target[trgi -> trgi = mxni]) = 0]")
        );
    }
}
