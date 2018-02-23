package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;

public class GoIRBinaryExprNode extends GoBaseIRNode {
	
	private String op;
	
	private GoBaseIRNode left;
	private GoBaseIRNode right;
	
	public GoIRBinaryExprNode(String op, GoBaseIRNode left, GoBaseIRNode right) {
		super("BinaryExpr");
		op = this.op;
		this.left = left;
		this.right = right;
	}
	
	@Override
	public void setChildParent() {
		left.setParent(this);
		right.setParent(this);
	}
	
	public String getOp() {
		return op;
	}
	
	public String toString() {
		return name;
	}
}
