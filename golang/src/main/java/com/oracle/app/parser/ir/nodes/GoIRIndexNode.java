package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRIndexNode extends GoBaseIRNode {

	GoIRIdentNode name;
	GoBaseIRNode index;
	String lbrack;
	String rbrack;
	
	public GoIRIndexNode(GoIRIdentNode name, GoBaseIRNode index, String lbrack, String rbrack) {
		super("Index Node");
		this.name = name;
		this.index = index;
		this.lbrack = lbrack;
		this.rbrack = rbrack;
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
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitIndexNode(this);
	}

}
