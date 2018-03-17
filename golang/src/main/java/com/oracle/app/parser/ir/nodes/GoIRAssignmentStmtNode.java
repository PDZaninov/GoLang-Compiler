package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRAssignmentStmtNode extends GoBaseIRNode {

	GoBaseIRNode lhs;
	GoBaseIRNode rhs;
	
	public GoIRAssignmentStmtNode(GoBaseIRNode lhs, GoBaseIRNode rhs){
		super("Assignment Statement Node");
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public String getIdentifier(){
		return lhs.getIdentifier();
	}
	
	public GoBaseIRNode getLHS(){
		return lhs;
	}
	
	public GoBaseIRNode getRHS(){
		return rhs;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitAssignment(this);
	}

}
