package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRCompositeLitNode extends GoBaseIRNode {

	GoBaseIRNode expr;
	String lbrace;
	GoIRArrayListExprNode elts;
	String rbrace;
	
	public GoIRCompositeLitNode(GoBaseIRNode expr, String lbrace, GoIRArrayListExprNode elts, String rbrace) {
		super("Composite Lit Node");
		this.expr = expr;
		this.lbrace = lbrace;
		this.elts = elts;
		this.rbrace = rbrace;
	}

	@Override
	public void setChildParent() {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visit(this);
	}

	public GoBaseIRNode getExpr() {
		return expr;
	}

	public String getLbrace() {
		return lbrace;
	}

	public GoIRArrayListExprNode getElts() {
		return elts;
	}

	public String getRbrace() {
		return rbrace;
	}

}
