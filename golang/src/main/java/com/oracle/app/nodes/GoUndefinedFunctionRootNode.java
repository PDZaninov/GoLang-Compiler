package com.oracle.app.nodes;

import com.oracle.app.GoLanguage;
import com.oracle.app.runtime.GoUndefinedNameException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

public class GoUndefinedFunctionRootNode extends GoRootNode {
    public GoUndefinedFunctionRootNode(GoLanguage language, String name) {
        super(language,null,null,null, null, null, name);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        throw GoUndefinedNameException.undefinedFunction(getName());
    }
}
