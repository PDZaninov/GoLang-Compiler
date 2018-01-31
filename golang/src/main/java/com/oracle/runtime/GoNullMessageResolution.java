package com.oracle.runtime;


import com.oracle.truffle.api.interop.CanResolve;
import com.oracle.truffle.api.interop.MessageResolution;
import com.oracle.truffle.api.interop.Resolve;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;

/**
 * The class containing all message resolution implementations of {@link SLNull}.
 */
/**
 * The class containing all message resolution implementations of {@link SLNull}.
 */
@MessageResolution(receiverType = GoNull.class)
public class GoNullMessageResolution {
    /*
     * An SL function resolves the IS_NULL message.
     */
    @Resolve(message = "IS_NULL")
    public abstract static class SLForeignIsNullNode extends Node {

        public Object access(Object receiver) {
            return GoNull.SINGLETON == receiver;
        }
    }

    @CanResolve
    public abstract static class CheckNull extends Node {

        protected static boolean test(TruffleObject receiver) {
            return receiver instanceof GoNull;
        }
    }
}