package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRBinaryExprNode extends GoBaseIRNode {
	
	String op;
	GoBaseIRNode left;
	GoBaseIRNode right;
	String source;
	
	public GoIRBinaryExprNode(String op, GoBaseIRNode left, GoBaseIRNode right, String source) {
		super("BinaryExpr");
		this.op = op;
		this.left = left;
		this.right = right;
		this.source = source;
	}
	
	public GoBaseIRNode getLeft() { return left; }
	
	public GoBaseIRNode getRight() { return right; }
	
	public String getOp() {
		return op;
	}
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitBinaryExpr(this); 
	}

}
