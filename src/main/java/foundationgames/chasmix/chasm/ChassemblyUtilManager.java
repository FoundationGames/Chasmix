package foundationgames.chasmix.chasm;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Adds utility code to a Chassembler when marked as required by other code.
 *
 * <p>EX: Helper functions, etc
 * <pre><code>
 * void generateSomething() {
 *     Chassembler chassembler = Chassembler.ofMap();
 *     ChassemblyUtilManager utilManager = new UtilityManager(chassembler);
 *
 *     utilManager.require(MyUtilites.MY_HELPER_FUNCTION);
 *     chassembler.put("my_key", "my_helper_function(foo)");
 * }
 *
 * </code></pre>
 */
public class ChassemblyUtilManager {
    private final Set<Utility> used = new HashSet<>();
    private final Chassembler chasm;

    public ChassemblyUtilManager(Chassembler chasm) {
        this.chasm = chasm;
    }

    public void require(Utility util) {
        if (!used.contains(util)) {
            used.add(util);
            util.libraryProvider().accept(this.chasm);
        }
    }

    public static record Utility(Consumer<Chassembler> libraryProvider) {
    }
}
