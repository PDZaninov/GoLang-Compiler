package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.app.parser.ir.GoIRFieldListNode;

public class GoIRStructTypeNode extends GoBaseIRNode {

    GoIRFieldListNode fieldList;
    boolean incomplete;

    public GoIRStrucutTypeNode(GoIRFieldListNode fieldList, boolean incomplete) {
        super("Struct Type node");
        this.fieldList = fieldList;
        this.incomplete = incomplete;
    }

    public GoIRFieldListNode getFieldListNode() { return fieldList; }

    public boolean getIncomplete() { return incomplete; }

    @Override
    public Object accept(GoIRVisitor visitor) { return visitor.visitStructType(this); }
}