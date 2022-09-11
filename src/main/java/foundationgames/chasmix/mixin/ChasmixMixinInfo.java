package foundationgames.chasmix.mixin;

import foundationgames.chasmix.mixin.util.Inspection;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.stream.Collectors;

public class ChasmixMixinInfo {
    public final String name;
    public final List<String> targets;
    public final int priority;
    public final boolean remap;
    public final boolean pseudo;

    public final List<String> interfaces;
    public final List<MixinField> fields;
    public final List<MixinMethod> methods;

    public ChasmixMixinInfo(String name, List<String> targets, int priority, boolean remap, boolean pseudo,
                            List<String> interfaces, List<MixinField> fields, List<MixinMethod> methods) {
        this.name = name;
        this.targets = targets;
        this.priority = priority;
        this.remap = remap;
        this.pseudo = pseudo;
        this.interfaces = interfaces;
        this.fields = fields;
        this.methods = methods;
    }

    public static ChasmixMixinInfo create(ClassNode mixinClass) {
        return Inspection.getAnnotation(mixinClass, Mixin.class, false).map(annotation -> {
            var targets = Inspection.<String>getAnnotationListValue(annotation, "targets");

            Inspection.<Type>getAnnotationListValue(annotation, "value").forEach(cls ->
                    targets.add(cls.getClassName().replaceAll("\\.", "/")));

            int priority = Inspection.<Integer>getAnnotationValue(annotation, "priority").orElse(1000);
            boolean remap = Inspection.<Boolean>getAnnotationValue(annotation, "remap").orElse(true);
            boolean pseudo = Inspection.getAnnotation(mixinClass, Pseudo.class, false).isPresent();

            var interfaces = mixinClass.interfaces;
            var fields = mixinClass.fields.stream().map(MixinField::new)
                    .collect(Collectors.toList());
            var methods = mixinClass.methods.stream().map(MixinMethod::new)
                    .collect(Collectors.toList());

            return new ChasmixMixinInfo(mixinClass.name, targets, priority, remap, pseudo, interfaces, fields, methods);
        }).orElse(null);
    }

    public static abstract class MixinMember {
        public final String name;
        public final String desc;
        public final int access;

        public boolean remapped;
        public boolean annShadow;
        public boolean annFinal;
        public boolean annMutable;
        public boolean annUnique;

        public MixinMember(String name, String desc, int access) {
            this.name = name;
            this.desc = desc;
            this.access = access;
        }
    }

    public static class MixinField extends MixinMember {
        public final FieldNode field;

        public MixinField(FieldNode field) {
            super(field.name, field.desc, field.access);
            this.field = field;

            if (Inspection.getAnnotation(field, Shadow.class, true).isPresent()) {
                this.annShadow = true;
                this.annFinal = Inspection.getAnnotation(field, Final.class, true).isPresent();
                this.annMutable = Inspection.getAnnotation(field, Mutable.class, true).isPresent();
            }
            this.annUnique = Inspection.getAnnotation(field, Unique.class, true).isPresent();
        }
    }

    public static class MixinMethod extends MixinMember {
        public final MethodNode method;

        public MixinMethod(MethodNode method) {
            super(method.name, method.desc, method.access);
            this.method = method;

            if (Inspection.getAnnotation(method, Shadow.class, true).isPresent()) {
                this.annShadow = true;
            }
            this.annUnique = Inspection.getAnnotation(method, Unique.class, true).isPresent();
        }
    }
}
