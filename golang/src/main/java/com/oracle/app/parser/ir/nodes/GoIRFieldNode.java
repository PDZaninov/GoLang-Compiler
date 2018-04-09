package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRFieldNode extends GoBaseIRNode {

	private GoBaseIRNode funcparam;
	
	public GoIRFieldNode(String name,GoBaseIRNode param ) {
		super(name);
		funcparam = param;
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
	public Object accept(GoIRVisitor visitor) {
		// TODO Auto-generated method stub
		return null;
	}

}