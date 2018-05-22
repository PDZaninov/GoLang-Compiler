package com.oracle.app.nodes;

import com.oracle.app.GoLanguage;
import com.oracle.app.runtime.GoContext;
import com.oracle.app.runtime.GoNull;
import com.oracle.truffle.api.frame.FrameDescriptor;
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
        super(language, frameDescriptor, null, bodyNode, sourceSection, name);
    }

    @Override
    public Object execute(VirtualFrame frame) {

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
