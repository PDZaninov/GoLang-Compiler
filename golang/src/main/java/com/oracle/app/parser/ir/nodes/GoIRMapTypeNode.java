package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRMapTypeNode extends GoBaseIRNode{

    GoBaseIRNode key;
    GoBaseIRNode value;

    public GoIRMapTypeNode(GoBaseIRNode key, GoBaseIRNode value){
        super("Map type node");
        this.key = key;
        this.value = value;
    }

    public GoBaseIRNode getKey() { return key; }

    public GoBaseIRNode getValue() { return value; }

    @Override
    public Object accept(GoIRVisitor visitor) { return visitor.visitMapType(this); }
}