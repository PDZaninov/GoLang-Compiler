package com.oracle.app.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.app.GoLanguage;
import com.oracle.runtime.GoFunction;
import com.oracle.runtime.GoUndefinedNameException;

/**
 * The initial {@link RootNode} of {@link SLFunction functions} when they are created, i.e., when
 * they are still undefined. Executing it throws an
 * {@link SLUndefinedNameException#undefinedFunction exception}.
 */
public class GoUndefinedFunctionRootNode extends GoRootNode {
    public GoUndefinedFunctionRootNode(GoLanguage language, String name) {
        super(language, null, null, null, name);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        throw GoUndefinedNameException.undefinedFunction(getName());
    }
}
