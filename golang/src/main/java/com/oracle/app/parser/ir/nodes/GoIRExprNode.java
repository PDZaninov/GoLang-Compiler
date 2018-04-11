package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRExprNode extends GoBaseIRNode {
	
	private GoIRArrayListExprNode child;
	
	public GoIRExprNode(GoIRArrayListExprNode child) {
		super("Expr");
		this.child = child;	
	}
	
	public GoIRArrayListExprNode getChild() {
		return child;
	}
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitExpr(this); 
	}
	
	
}
