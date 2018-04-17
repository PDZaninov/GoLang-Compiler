package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRIndexNode extends GoBaseIRNode {

	GoBaseIRNode name;
	GoBaseIRNode index;
	String lbrack;
	String rbrack;
	
	public GoIRIndexNode(GoBaseIRNode name, GoBaseIRNode index, String lbrack, String rbrack) {
		super("Index Node");
		this.name = name;
		this.index = index;
		this.lbrack = lbrack;
		this.rbrack = rbrack;
	}
	
	public GoBaseIRNode getName(){
		return name;
	}
	
	@Override
	public String getIdentifier(){
		return name.getIdentifier();
	}
	
	public GoBaseIRNode getIndex(){
		return index;
	}
	
	public int getLineNumber(){
		return Integer.parseInt(rbrack.split(":")[1]);
	}
	
	public int getRBrack(){
		return Integer.parseInt(rbrack.split(":")[2]);
	}
	
	public int getLBrack(){
		return Integer.parseInt(lbrack.split(":")[2]);
	}
	
	public int getSourceSize(){
		int start = Integer.parseInt(lbrack.split(":")[2]);
		int end = Integer.parseInt(rbrack.split(":")[2]);
		return end - start;
	}
	
	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitIndexNode(this);
	}

}
