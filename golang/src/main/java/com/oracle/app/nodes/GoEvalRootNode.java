package com.oracle.app.nodes;

import java.util.Map;

import com.oracle.app.GoLanguage;
import com.oracle.app.runtime.GoContext;
import com.oracle.app.runtime.GoNull;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.TruffleLanguage.ContextReference;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;

/**
 * In addition to {@link GoRootNode}, this class performs two additional tasks:
 *
 * <ul>
 * <li>Lazily registration of functions on first execution. This fulfills the semantics of
 * "evaluating" source code in Go.</li>
 * <li>Conversion of arguments to types understood by Go. The Go source code can be evaluated from a
 * different language, i.e., the caller can be a node from a different language that uses types not
 * understood by Go.</li>
 * </ul>
 */
public final class GoEvalRootNode extends GoRootNode {

    public GoEvalRootNode(GoLanguage language, FrameDescriptor frameDescriptor, GoExpressionNode bodyNode, SourceSection sourceSection, String name) {
        super(language, frameDescriptor, null,null, bodyNode, sourceSection, name);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        /* Lazy registrations of functions on first execution. 
        if (!registered) {
             Function registration is a Goow-path operation that must not be compiled. And default variable initialization
            CompilerDirectives.transferToInterpreterAndInvalidate();
            reference.get().getFunctionRegistry().register(functions);
            registered = true;
            FrameDescriptor f = getFrameDescriptor();
            FrameSlot slot;
            slot = f.findFrameSlot("int");
            slot.setKind(FrameSlotKind.Int);
            frame.setInt(slot, 0);
            slot = f.findFrameSlot("float64");
            frame.setDouble(slot, 0);
            slot = f.findFrameSlot("float32");
            frame.setFloat(slot, 0);
            slot = f.findFrameSlot("string");
            frame.setObject(slot, "");
            slot = f.findFrameSlot("bool");
            frame.setBoolean(slot, false);
            slot = f.findFrameSlot("true");
            frame.setBoolean(slot,true);
            slot = f.findFrameSlot("false");
            frame.setBoolean(slot, false);
        }
         */
        if (getBodyNode() == null) {
            /* The source code did not have a "main" function, so nothing to execute. */
        	System.out.println("FAILURE NO BODYNODE");
            return GoNull.SINGLETON;
        }

        /* Conversion of arguments to types understood by Go. */
        Object[] arguments = frame.getArguments();
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = GoContext.fromForeignValue(arguments[i]);
        }

        /* Now we can execute the body of the "main" function. */
        return super.execute(frame);
    }
}
