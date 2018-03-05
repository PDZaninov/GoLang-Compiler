package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRArrayTypeNode extends GoBaseIRNode{
	GoBaseIRNode len;
	GoBaseIRNode type;
	
	public GoIRArrayTypeNode(String name,GoBaseIRNode length, GoBaseIRNode type) {
		super(name);
		len = length;
		this.type = type;
		setChildParent();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setChildParent() {
		len.setParent(this);
		type.setParent(this);
		
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		// TODO Auto-generated method stub
		return null;
	}

}
