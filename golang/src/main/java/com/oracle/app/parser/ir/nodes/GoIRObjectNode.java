package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitable;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRObjectNode extends GoBaseIRNode {

    GoBaseIRNode functionNode;
    String kind;

    public GoIRObjectNode(GoBaseIRNode functionNode, String kind) {
        super("Object Node");
        this.functionNode = functionNode;
        this.kind = kind;
    }

    public GoBaseIRNode getFunctionNode() {
        return functionNode;
    }

    public String getKind() {
        return kind;
    }

    @Override
    public Object accept(GoIRVisitor visitor) {
        return visitor.visitObjectNode(this);
    }

}
