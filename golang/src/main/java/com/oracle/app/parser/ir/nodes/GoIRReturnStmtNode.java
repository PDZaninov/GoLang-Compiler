package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRReturnStmtNode  extends GoBaseIRNode{
	GoIRArrayListExprNode children;
	
	public GoIRReturnStmtNode(GoIRArrayListExprNode children) {
		super("ArrayList Expression Node");
		this.children = children;
		setChildParent();
	}

	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitReturnStmt(this);
	}

	@Override
	public void setChildParent() {
		// TODO Auto-generated method stub
		
	}
	
	public GoIRArrayListExprNode getChild() {
		return children;
		
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
