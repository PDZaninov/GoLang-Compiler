package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRAssignmentStmtNode extends GoBaseIRNode {

	GoBaseIRNode lhs;
	GoBaseIRNode rhs;
	
	public GoIRAssignmentStmtNode(GoBaseIRNode lhs, GoBaseIRNode rhs){
		super("Assignment Statement Node");
		this.lhs = lhs;
		this.rhs = rhs;
		setChildParent();
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
	public void setChildParent() {
		lhs.setParent(this);
		rhs.setParent(this);
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		return null;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitAssignment(this);
	}

}
