package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRReturnStmtNode  extends GoBaseIRNode{
	GoIRArrayListExprNode children;
	
	public GoIRReturnStmtNode(GoIRArrayListExprNode children) {
		super("ArrayList Expression Node");
		this.children = children;
	}

	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitReturnStmt(this);
	}
	
	public GoIRArrayListExprNode getChild() {
		return children;
		
	}

}
