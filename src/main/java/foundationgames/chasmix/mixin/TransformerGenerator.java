package foundationgames.chasmix.mixin;

import foundationgames.chasmix.api.TransformerHolder;
import foundationgames.chasmix.chasm.Chassembler;
import foundationgames.chasmix.chasm.ChassemblyUtilManager;
import foundationgames.chasmix.chasm.TransformerHolderImpl;
import foundationgames.chasmix.mixin.util.Constants;
import foundationgames.chasmix.mixin.util.Utilities;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldNode;
import org.quiltmc.chasm.lang.api.ast.ListNode;
import org.quiltmc.chasm.lang.api.ast.Node;
import org.quiltmc.chasm.lang.api.ast.TernaryNode;

import java.util.ArrayList;

public class TransformerGenerator {
    public final ChasmixMixinInfo mixin;
    public final ChasmixTargetInfo target;

    private final Chassembler chasm = Chassembler.ofMap();
    private final Chassembler transformations = Chassembler.ofList();

    private final ChassemblyUtilManager util = new ChassemblyUtilManager(chasm);
    private FieldNode n;

    public TransformerGenerator(ChasmixMixinInfo mixin, ChasmixTargetInfo target) {
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

        // Fields
        if (this.mixin.fields.size() > 0) {
            this.mergeFields();
        }

        // Methods
        if (this.mixin.methods.size() > 0) {
            this.mergeMethods();
        }

        chasm.put("transformations", this.mixin.pseudo ?
                new TernaryNode(Node.parse("trg = null"), new ListNode(new ArrayList<>()), this.transformations.asNode())
                : this.transformations.asNode());

        var name = this.mixin.name;
        int split = name.lastIndexOf("/");
        return new TransformerHolderImpl(
                name.substring(0, split < 0 ? name.length() : split),
                name.substring(split + 1),
                this.chasm.asNode());
    }

    /* ~~ Merge-All Methods ~~ */

    public void mergeInterfaces() {
        transformations.addPrependTransform(
                Node.parse("trg.interfaces"),
                Node.parse("args -> mxn.interfaces[mxni -> len(args.target[trgi -> trgi = mxni]) = 0]")
        );
    }

    public void mergeFields() {
        for (var field : this.mixin.fields) {
            if (field.annShadow) {
                this.shadowField(field);
            } else {
                this.addField(field);
            }
        }
    }

    public void mergeMethods() {
        for (var method : this.mixin.methods) {
            if (method.annShadow) {
                this.shadowMethod(method);
            } else if (!"<init>".equals(method.name)) {
                if (Constants.CLINIT.equals(method.name)) {
                    this.addClinit();
                } else {
                    this.addMethod(method);
                }
            }
        }
    }

    /* ~~ Individual Processor Adding Methods ~~ */

    public void addField(ChasmixMixinInfo.MixinField field) {
        transformations.addPrependTransform(
                Node.parse("trg.fields"),
                Node.parse("x -> mxn.fields[mxnf -> mxnf.name = \"" + field.name + "\"]"));
    }

    public void shadowField(ChasmixMixinInfo.MixinField field) {
        this.target.expect(
                cls -> cls.fields.stream().anyMatch(trgf -> field.name.equals(trgf.name)),
                "Mixin '" + this.mixin.name + "' shadows field '" + field.name
                      + "', but it is not present in the target class");

        if (field.annMutable) {
            transformations.addNodeTransform(
                    Node.parse("trg.fields[trgf -> trgf.name = \"" + field.name + "\"][0].access"),
                    Node.parse("args -> args.target & " + ~Opcodes.ACC_FINAL));
        }
        // TODO: Merge annotations
    }

    public void addMethod(ChasmixMixinInfo.MixinMethod method) {
        this.util.require(Utilities.REPARENT);
        this.util.require(Utilities.METHOD_DESC);

        transformations.addPrependTransform(
                Node.parse("trg.methods"),
                Node.parse("x -> [method_reparent(mxn.methods[mxnm -> mxnm.name = \"" + method.name
                        + "\" && method_desc(mxnm) = \""+ method.desc + "\"][0])]")
        );
    }

    public void shadowMethod(ChasmixMixinInfo.MixinMethod method) {
        this.target.expect(
                cls -> cls.methods.stream().anyMatch(
                        trgm -> method.name.equals(trgm.name) && method.desc.equals(trgm.desc)),
                "Mixin '" + this.mixin.name + "' shadows method '" + method.name + " " + method.desc
                        + "', but it is not present in the target class");

        // TODO: Merge annotations
    }

    public void addClinit() {
        this.util.require(Utilities.REPARENT);

        transformations.addConditionalTransform(
                Node.parse("len(trg.methods[trgm -> trgm.name = \"<clinit>\"]) > 0"),
                c -> c.addInsnsTailTransform(
                        Node.parse("trg.methods[trgm -> trgm.name = \"<clinit>\"][0].code.instructions"),
                        Node.parse(
                                "x -> insn_reparent(mxn.methods[mxnm -> mxnm.name = \"<clinit>\"][0]"
                              + ".code.instructions[i -> i.opcode != " + Opcodes.RETURN + "])"
                        )
                ),
                c -> c.addPrependTransform(Node.parse("trg.methods"),
                        Node.parse("x -> [method_reparent(mxn.methods[mxnm -> (mxnm.name = \"<clinit>\")][0])]"))
        );
    }
}
