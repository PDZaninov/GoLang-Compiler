package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRIndexNode extends GoBaseIRNode {

	GoIRIdentNode name;
	GoBaseIRNode index;
	
	public GoIRIndexNode(GoIRIdentNode name, GoBaseIRNode index) {
		super("Index Node");
		this.name = name;
		this.index = index;
		setChildParent();
	}
	
	public GoIRIdentNode getName(){
		return name;
	}
	
	@Override
	public String getIdentifier(){
		return name.getIdentifier();
	}
	
	public GoBaseIRNode getIndex(){
		return index;
	}

	@Override
	public void setChildParent() {
		name.setParent(this);
		index.setParent(this);
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitIndexNode(this);
	}

}
