package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRIdentNode extends GoBaseIRNode {
	
	String ident;
	GoBaseIRNode child;
	String namepos;
	
	public GoIRIdentNode(String ident, GoBaseIRNode child, String namepos) {
		super("Ident");
		this.ident = ident;
		this.child = child;
		this.namepos = namepos;
	}

	//Will need to merge getIdent and getIdentifier so that only getIdentifier is used
	@Override
	public String getIdentifier(){
		return ident;
	}
	
	public String getIdent() {
		return ident;
	}
	
	public GoBaseIRNode getChild() {
		return child;
	}
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitIdent(this); 
	}
	
	
}
