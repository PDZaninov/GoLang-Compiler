package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRUnaryNode extends GoBaseIRNode {
	
	String op;
	String source;
	GoBaseIRNode child;
	
	public GoIRUnaryNode(String op, GoBaseIRNode child,String source) {
		super("Unary");
		this.op = op;
		this.child = child;
		this.source = source;
	}
	
	public GoBaseIRNode getChild() { return child; }
	
	public String getOp() {
		return op;
	}
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitUnary(this); 
	}

}
