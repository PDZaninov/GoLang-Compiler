package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRStructTypeNode extends GoBaseIRNode {

    GoIRFieldListNode fieldList;
    boolean incomplete;

    public GoIRStructTypeNode(GoIRFieldListNode fieldList, String incomplete) {
        super("Struct Type node");
        this.fieldList = fieldList;
        this.incomplete = Boolean.getBoolean(incomplete);
    }

    public GoIRFieldListNode getFieldListNode() { return fieldList; }

    public boolean getIncomplete() { return incomplete; }

    @Override
    public Object accept(GoIRVisitor visitor) { 
    	return visitor.visitStructType(this); 
    }
}