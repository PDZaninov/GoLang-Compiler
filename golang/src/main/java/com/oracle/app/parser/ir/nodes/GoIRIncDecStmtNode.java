package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRIncDecStmtNode extends GoBaseIRNode {
	
	String op;
	GoBaseIRNode child;
	String tokpos;
	
	public GoIRIncDecStmtNode(String op, GoBaseIRNode child, String tokpos) {
		super("IncDec");
		this.op = op;
		this.child = child;
		this.tokpos = tokpos;
	}
	
	public GoBaseIRNode getChild() { 
		return child; 
	}
	
	public String getTokPos(){
		return tokpos;
	}
	
	public String getOp() {
		return op;
	}
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitIncDecStmt(this); 
	}

}
