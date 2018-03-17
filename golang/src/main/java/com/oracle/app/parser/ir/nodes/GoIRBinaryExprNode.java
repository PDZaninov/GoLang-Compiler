package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

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
		setChildParent();
		this.source = source;
	}
	
	@Override
	public void setChildParent() {
		left.setParent(this);
		right.setParent(this);
	}
	
	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
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
