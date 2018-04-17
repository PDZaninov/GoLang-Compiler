package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

//Not sure if Gen Decl handles type spec yet since we do not have a spec node. It seems to be array list expression node.

public class GoIRTypeSpecNode extends GoBaseIRNode {

    private GoBaseIRNode name;
    private GoBaseIRNode type;

    public GoIRTypeSpecNode(GoBaseIRNode name, GoBaseIRNode type){
        super("Type Spec Node");
        this.name = name;
        this.type = type;
    }

    public GoBaseIRNode getName() { return name; }

    public GoBaseIRNode getType() { return type; }

    @Override
    public Object accept(GoIRVisitor visitor) {  
    	return visitor.visitTypeSpec(this);
    }
}