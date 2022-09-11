package foundationgames.chasmix.chasm;

import org.quiltmc.chasm.lang.api.ast.CallNode;
import org.quiltmc.chasm.lang.api.ast.IntegerNode;
import org.quiltmc.chasm.lang.api.ast.LambdaNode;
import org.quiltmc.chasm.lang.api.ast.ListNode;
import org.quiltmc.chasm.lang.api.ast.MapNode;
import org.quiltmc.chasm.lang.api.ast.MemberNode;
import org.quiltmc.chasm.lang.api.ast.Node;
import org.quiltmc.chasm.lang.api.ast.NullNode;
import org.quiltmc.chasm.lang.api.ast.ReferenceNode;
import org.quiltmc.chasm.lang.api.ast.TernaryNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

// TODO: Better error handling
public class Chassembler {
    private final Deque<Node> nodeStack = new ArrayDeque<>();

    private Chassembler() {
    }

    public static Chassembler ofMap() {
        return new Chassembler().init(new MapNode(new LinkedHashMap<>()));
    }

    public static Chassembler ofList() {
        return new Chassembler().init(new ListNode(new ArrayList<>()));
    }

    private Chassembler init(Node first) {
        this.nodeStack.addFirst(first);
        return this;
    }

    public Node top() {
        return nodeStack.getFirst();
    }

    public Chassembler putMap(String key) {
        var node = new MapNode(new LinkedHashMap<>());
        this.put(key, node);
        this.nodeStack.addFirst(node);
        return this;
    }

    public Chassembler putList(String key) {
        var node = new ListNode(new ArrayList<>());
        this.put(key, node);
        this.nodeStack.addFirst(node);
        return this;
    }

    public Chassembler addMap() {
        var node = new MapNode(new LinkedHashMap<>());
        this.add(node);
        this.nodeStack.addFirst(node);
        return this;
    }

    public Chassembler addList() {
        var node = new ListNode(new ArrayList<>());
        this.add(node);
        this.nodeStack.addFirst(node);
        return this;
    }

    public Chassembler put(String key, Node value) {
        if (top() instanceof MapNode map) {
            map.getEntries().put(key, value);
        }

        return this;
    }

    public Chassembler put(String key, String expr) {
        return put(key, Node.parse(expr));
    }

    public Chassembler add(Node value) {
        if (top() instanceof ListNode list) {
            list.getEntries().add(value);
        }

        return this;
    }

    public Chassembler add(String expr) {
        return add(Node.parse(expr));
    }

    public Chassembler putClass(String key, String className) {
        return this.put(key, Node.parse("classes[c -> c.name = \""+className+"\"][0]"));
    }

    public Chassembler addConditionalTransform(Node condition,
                                               Consumer<Chassembler> ifTrue, Consumer<Chassembler> ifFalse) {
        var map = new LinkedHashMap<String, Node>();

        var temp = ofList();
        ifTrue.accept(temp);
        if (temp.asNode() instanceof ListNode list) {
            map.put("r_true", new LambdaNode("x", list.getEntries().get(0)));
        }

        temp = ofList();
        ifFalse.accept(temp);
        if (temp.asNode() instanceof ListNode list) {
            map.put("r_false", new LambdaNode("x", list.getEntries().get(0)));
        }

        map.put("result", new TernaryNode(condition,
                new CallNode(new ReferenceNode("r_true", false), new NullNode()),
                new CallNode(new ReferenceNode("r_false", false), new NullNode())));

        return this.add(new MemberNode(new MapNode(map), "result"));
    }

    public Chassembler addSliceTransform(Node target, Node start, Node end, Node apply) {
        return this.addMap()
                .putMap("target")
                    .put("node", target)
                    .put("start", start)
                    .put("end", end)
                .end()
                .put("apply", apply)
            .end();
    }

    public Chassembler addPrependTransform(Node target, Node apply) {
        return this.addSliceTransform(target, new IntegerNode(0), new IntegerNode(0), apply);
    }

    public Chassembler addAppendTransform(Node target, Node apply) {
        return this.addSliceTransform(target, Node.parse("2 * len(node) + 1"), Node.parse("start"), apply);
    }

    public Chassembler addInsnsTailTransform(Node target, Node apply) {
        return this.addSliceTransform(target, Node.parse("2 * len(node) - 1"), Node.parse("start"), apply);
    }

    public Chassembler addNodeTransform(Node target, Node apply) {
        return this.addMap()
                .putMap("target")
                    .put("node", target)
                .end()
                    .put("apply", apply)
                .end();
    }

    public Chassembler end() {
        nodeStack.removeFirst();
        return this;
    }

    public Node asNode() {
        return this.nodeStack.getLast();
    }
}
