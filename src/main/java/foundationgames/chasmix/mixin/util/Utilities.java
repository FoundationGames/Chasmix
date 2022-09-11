package foundationgames.chasmix.mixin.util;

import foundationgames.chasmix.chasm.ChassemblyUtilManager;

public final class Utilities {
    public static final ChassemblyUtilManager.Utility REPARENT = new ChassemblyUtilManager.Utility(chasm -> {
        chasm.put("insn_reparent",
                "i -> map({list: i, func: ix -> ix.owner = mxn.name ? ix + {owner: trg.name} : ix})");
        chasm.put("method_reparent",
                "m -> m + {code: m.code + {instructions: insn_reparent(m.code.instructions)}}");
    });

    public static final ChassemblyUtilManager.Utility REVERSE = new ChassemblyUtilManager.Utility(chasm -> {
        chasm.put("reverse",
                "l -> {fun: prev -> prev.idx = 0 ? prev.acc : fun({lst: prev.lst, idx: prev.idx - 1, acc: prev.acc + [lst[idx]]}), res: fun({lst: l, idx: len(lst), acc: []})}.res");
    });

    public static final ChassemblyUtilManager.Utility METHOD_DESC = new ChassemblyUtilManager.Utility(chasm -> {
        chasm.put("method_desc", "m -> \"(\" + reduce({list: map({list: m.parameters, func: p -> p.type}) + [\"\"], func: p -> p.first + p.second}) + \")\" + m.returnType");
    });

    private Utilities() {
    }
}
