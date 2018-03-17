package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRBranchStmtNode extends GoBaseIRNode {
	
	String type;
	String source;
	GoBaseIRNode child;
	
	public GoIRBranchStmtNode(String type,GoBaseIRNode child, String source) {
		super("BranchStmt");
		this.type = type;
		this.child = child;
		this.source = source;
	}
	
	public String getType() {
		return type;
	}
	
	public GoBaseIRNode getChild() { return child; }
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitBranchStmt(this); 
	}

}
