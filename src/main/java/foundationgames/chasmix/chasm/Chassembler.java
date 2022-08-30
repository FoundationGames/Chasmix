package foundationgames.chasmix.chasm;

import org.quiltmc.chasm.lang.api.ast.IntegerNode;
import org.quiltmc.chasm.lang.api.ast.ListNode;
import org.quiltmc.chasm.lang.api.ast.MapNode;
import org.quiltmc.chasm.lang.api.ast.Node;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;

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

    public Chassembler addTransform(Node target, Node start, Node end, Node apply) {
        return this.addMap()
                .putMap("target")
                    .put("node", target)
                    .put("start", start)
                    .put("end", end)
                .end()
                .put("apply", apply)
            .end();
    }

    public Chassembler addTransform(Node target, Node apply) {
        return this.addTransform(target, new IntegerNode(0), new IntegerNode(0), apply);
    }

    public Chassembler end() {
        nodeStack.removeFirst();
        return this;
    }

    public Node asNode() {
        return this.nodeStack.getLast();
    }
}
