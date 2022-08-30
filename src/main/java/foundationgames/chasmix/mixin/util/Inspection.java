package foundationgames.chasmix.mixin.util;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class Inspection {
    public static Optional<AnnotationNode> getAnnotation(FieldNode field, Class<? extends Annotation> annotation, boolean visible) {
        return findNodeWithDesc(Type.getDescriptor(annotation),
                visible ? field.visibleAnnotations : field.invisibleAnnotations, node -> node.desc);
    }

    public static Optional<AnnotationNode> getAnnotation(ClassNode clazz, Class<? extends Annotation> annotation, boolean visible) {
        return findNodeWithDesc(Type.getDescriptor(annotation),
                visible ? clazz.visibleAnnotations : clazz.invisibleAnnotations, node -> node.desc);
    }

    public static Optional<AnnotationNode> getAnnotation(MethodNode method, Class<? extends Annotation> annotation, boolean visible) {
        return findNodeWithDesc(Type.getDescriptor(annotation),
                visible ? method.visibleAnnotations : method.invisibleAnnotations, node -> node.desc);
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getAnnotationValue(AnnotationNode annotation, String key) {
        if (annotation.values == null) {
            return Optional.empty();
        }

        var iter = annotation.values.iterator();
        // Keys and values are stored like such:
        // [key, value, key2, value2, key3, value3, etc]

        while (iter.hasNext()) {
            // Check if the current element matches the key, and if so, the next element should be the value
            if (key.equals(iter.next())) {
                try {
                    return Optional.of((T) iter.next());
                } catch (ClassCastException ex) {
                    return Optional.empty();
                }
            } else {
                iter.next();
            }
        }

        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getAnnotationListValue(AnnotationNode annotation, String key) {
        var val = Inspection.getAnnotationValue(annotation, key);

        return val.map(obj -> {
            if (obj instanceof List) {
                return (List<T>)obj;
            }

            return new ArrayList<T>();
        }).orElseGet(ArrayList::new);
    }

    public static <V> Optional<V> findNodeWithDesc(String desc, List<V> nodes, Function<V, String> descGetter) {
        if (nodes == null) {
            return Optional.empty();
        }

        return nodes.stream().filter(node -> desc.equals(descGetter.apply(node))).findFirst();
    }

    private Inspection() {}
}
