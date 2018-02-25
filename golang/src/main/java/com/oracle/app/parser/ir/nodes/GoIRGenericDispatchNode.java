package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRGenericDispatchNode extends GoBaseIRNode {
	
	public GoIRGenericDispatchNode() {
		super("Generic Dispatch");
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
		return visitor.visitGenericDispatch(this); 
	}
	

}
