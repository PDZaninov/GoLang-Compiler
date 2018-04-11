package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.app.parser.ir.GoIRFieldNode;

public class GoIRFieldListNode extends GoBaseIRNode {

    private GoIRArrayListExprNode fields;

    public GoIRFieldListNode(GoIRArrayListExprNode fields){
        super("Field List node");
        this.fields = fields;
    }

    public GoIRArrayListExprNode getFields() { return fields; }

    @Override
    public Object accept(GoIRVisitor visitor) { return visitor.visitFieldList(this); }

}