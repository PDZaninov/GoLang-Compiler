package com.oracle.app.parser.ir.nodes;

import com.oracle.app.GoException;
import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

public class GoIRBinaryExprNode extends GoBaseIRNode {
	
	String op;
	GoBaseIRNode left;
	GoBaseIRNode right;
	String source;
	String type = null;
	
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
	
	public String getType() {
		return type;
	}
	
	public void setType(String a) {
		type = a;
	}


}
