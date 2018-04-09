package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

import java.util.ArrayList;

public class GoIRImportSpecNode extends GoBaseIRNode {

    private GoIRBasicLitNode child;

    public GoIRImportSpecNode(GoIRBasicLitNode child) {
        super("ImportSpec");
        this.child = child;
        setChildParent();
    }

    @Override
    public void setChildParent() {
        if(child != null)
            child.setParent(this);
    }

    @Override
    public ArrayList<GoBaseIRNode> getChildren() {
        return null;
    }

    public GoIRBasicLitNode getChild() {
        return child;
    }

    @Override
    public Object accept(GoIRVisitor visitor) {
        return visitor.visitImportSpec(this);
    }
}
