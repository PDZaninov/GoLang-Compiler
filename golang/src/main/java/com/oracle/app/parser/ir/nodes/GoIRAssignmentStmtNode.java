package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRAssignmentStmtNode extends GoBaseIRNode {

	GoBaseIRNode lhs;
	GoBaseIRNode rhs;
	GoBaseIRNode type;
	
	public GoIRAssignmentStmtNode(GoBaseIRNode lhs, GoBaseIRNode rhs, GoBaseIRNode type){
		super("Assignment Statement Node");
		this.lhs = lhs;
		this.rhs = rhs;
		this.type = type;
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

	public GoBaseIRNode getType() {
		return type;
	}
	
	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitAssignment(this);
	}

}
