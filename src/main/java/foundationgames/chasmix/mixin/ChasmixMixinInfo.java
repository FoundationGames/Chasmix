package foundationgames.chasmix.mixin;

import foundationgames.chasmix.mixin.util.Inspection;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.stream.Collectors;

public class ChasmixMixinInfo {
    public final String name;
    public final List<String> targets;
    public final int priority;
    public final boolean remap;

    public final List<String> interfaces;
    public final List<MixinField> fields;

    public ChasmixMixinInfo(String name, List<String> targets, int priority, boolean remap,
                            List<String> interfaces, List<MixinField> fields) {
        this.name = name;
        this.targets = targets;
        this.priority = priority;
        this.remap = remap;
        this.interfaces = interfaces;
        this.fields = fields;
    }

    public static ChasmixMixinInfo create(ClassNode mixinClass) {
        return Inspection.getAnnotation(mixinClass, Mixin.class, false).map(annotation -> {
            var targets = Inspection.<String>getAnnotationListValue(annotation, "targets");

            Inspection.<Type>getAnnotationListValue(annotation, "value").forEach(cls ->
                    targets.add(cls.getClassName().replaceAll("\\.", "/")));

            int priority = Inspection.<Integer>getAnnotationValue(annotation, "priority").orElse(1000);
            boolean remap = Inspection.<Boolean>getAnnotationValue(annotation, "remap").orElse(true);

            var interfaces = mixinClass.interfaces;
            var fields = mixinClass.fields.stream().map(MixinField::new)
                    .collect(Collectors.toList());

            return new ChasmixMixinInfo(mixinClass.name, targets, priority, remap, interfaces, fields);
        }).orElse(null);
    }

    public static abstract class MixinMember {
        public final String name;
        public final String desc;
        public final int access;

        public boolean remapped;
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
                this.annFinal = Inspection.getAnnotation(field, Final.class, true).isPresent();
                this.annMutable = Inspection.getAnnotation(field, Mutable.class, true).isPresent();
            }
            this.annUnique = Inspection.getAnnotation(field, Unique.class, true).isPresent();
        }
    }
}
