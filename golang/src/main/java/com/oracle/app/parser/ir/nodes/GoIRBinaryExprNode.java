package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

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

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		System.out.println("Kyle can do this getchildren thing since he made this node. But I also took the time to write this.");
		return null;
	}
}
