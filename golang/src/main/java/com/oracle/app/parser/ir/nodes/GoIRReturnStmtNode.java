package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRReturnStmtNode  extends GoBaseIRNode{
	GoIRArrayListExprNode children;
	int index;
	String returnpos;
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int i) {
		index = i;
	}
	
	public GoIRReturnStmtNode(GoIRArrayListExprNode children, String returnpos) {
		super("ArrayList Expression Node");
		this.children = children;
		this.returnpos = returnpos;
	}

	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitReturnStmt(this);
	}
	
	public int getReturnPosLineNum(){
		String[] split = returnpos.split(":");
		return Integer.parseInt(split[1]);
	}
	
	public int getReturnPosStartColumn(){
		String[] split = returnpos.split(":");
		return Integer.parseInt(split[2]);
	}
	
	public GoIRArrayListExprNode getChild() {
		return children;
		
	}
	

}
