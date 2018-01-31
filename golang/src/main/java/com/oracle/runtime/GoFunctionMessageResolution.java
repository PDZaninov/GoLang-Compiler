package com.oracle.runtime;

import static com.oracle.runtime.GoContext.fromForeignValue;

import com.oracle.truffle.api.interop.CanResolve;
import com.oracle.truffle.api.interop.MessageResolution;
import com.oracle.truffle.api.interop.Resolve;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.app.nodes.call.GoDispatchNode;
import com.oracle.app.nodes.call.GoDispatchNodeGen;
import com.oracle.app.nodes.interop.GoTypeToForeignNode;
import com.oracle.app.nodes.interop.GoTypeToForeignNodeGen;

/**
 * The class containing all message resolution implementations of {@link SLFunction}.
 */
/**
 * The class containing all message resolution implementations of {@link SLFunction}.
 */
@MessageResolution(receiverType = GoFunction.class)
public class GoFunctionMessageResolution {
    /*
     * An SL function resolves an EXECUTE message.
     */
    @Resolve(message = "EXECUTE")
    public abstract static class SLForeignFunctionExecuteNode extends Node {

        @Child private GoDispatchNode dispatch = GoDispatchNodeGen.create();
        @Child private GoTypeToForeignNode toForeign = GoTypeToForeignNodeGen.create();

        public Object access(GoFunction receiver, Object[] arguments) {
            Object[] arr = new Object[arguments.length];
            // Before the arguments can be used by the SLFunction, they need to be converted to SL
            // values.
            for (int i = 0; i < arr.length; i++) {
                arr[i] = fromForeignValue(arguments[i]);
            }
            Object result = dispatch.executeDispatch(receiver, arr);
            return toForeign.executeConvert(result);
        }
    }

    /*
     * An SL function should respond to an IS_EXECUTABLE message with true.
     */
    @Resolve(message = "IS_EXECUTABLE")
    public abstract static class SLForeignIsExecutableNode extends Node {
        public Object access(Object receiver) {
            return receiver instanceof GoFunction;
        }
    }

    @CanResolve
    public abstract static class CheckFunction extends Node {

        protected static boolean test(TruffleObject receiver) {
            return receiver instanceof GoFunction;
        }
    }
}