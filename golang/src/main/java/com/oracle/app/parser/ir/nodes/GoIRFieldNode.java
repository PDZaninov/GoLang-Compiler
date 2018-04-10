package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRFieldNode extends GoBaseIRNode {

	private GoBaseIRNode ident;//ident type, ident name for var
	private GoBaseIRNode type;// ident type, ident of type for var
	
	public GoIRFieldNode(String name,GoBaseIRNode ident, GoBaseIRNode type) {
		super(name);
		this.ident = ident;
		this.type = type;
	}

	@Override
	public void setChildParent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitField(this);
	}

}