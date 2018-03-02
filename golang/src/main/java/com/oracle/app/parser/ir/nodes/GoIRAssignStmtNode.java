package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRAssignStmtNode extends GoBaseIRNode {

	private GoIRArrayListExprNode lhs;
	private String 				  token;
	private GoIRArrayListExprNode rhs;
	
	public GoIRAssignStmtNode(GoIRArrayListExprNode lhs, String token, GoIRArrayListExprNode rhs) {
		super("Assignment Node");
		this.lhs 	= lhs;
		this.token  = token;
		this.rhs 	= rhs;
		setChildParent();
	}
	
	public GoIRArrayListExprNode getLeft(){
		return lhs;
	}
	
	public String getToken(){
		return token;
	}
	
	public GoIRArrayListExprNode getRight(){
		return rhs;
	}

	@Override
	public void setChildParent() {
		lhs.setParent(this);
		rhs.setParent(this);
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitAssignStmt(this);
	}

}
