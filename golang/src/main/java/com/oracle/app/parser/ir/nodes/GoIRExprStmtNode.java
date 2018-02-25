package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRExprStmtNode extends GoBaseIRNode {

	GoBaseIRNode expression;
	
	public GoIRExprStmtNode(GoBaseIRNode expression) {
		super("Stand alone Expression");
		this.expression = expression;
		setChildParent();
	}
	
	public GoBaseIRNode getChild() {
		return expression;
	}

	@Override
	public void accept(GoIRVisitor visitor){
		visitor.visitExprStmt(this);
	}
	
	@Override
	public void setChildParent() {
		expression.setParent(this);

	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

}
