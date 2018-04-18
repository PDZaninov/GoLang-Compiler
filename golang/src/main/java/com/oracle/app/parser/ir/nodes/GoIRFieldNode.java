package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRFieldNode extends GoBaseIRNode {

	private GoIRArrayListExprNode names;
	private GoIRIdentNode type;
	
	public GoIRFieldNode(String name,GoIRArrayListExprNode names, GoIRIdentNode type) {
		super(name);
		this.names = names;
		this.type = type;
	}

	public GoIRFieldNode(String name, GoIRIdentNode type) {
		super(name);
		this.type = type;
	}
	
	public GoIRArrayListExprNode getNames() {
		return names;
	}
	
	public GoIRIdentNode getType() {
		return type;
	}
	
	public String getTypeName() {
		if(type != null) {
			return type.getIdentifier();
		}
		return null;
	}

	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitField(this);
	}

}