package com.oracle.app.parser.ir;

public class GoBaseIRNode {
	
	private String name;

	private GoBaseIRNode parent;
	
	public GoBaseIRNode(String name) {
		this.name = name;
	}
	
	public GoBaseIRNode getParent() {
		return parent;
	}
	
	public Object accept(GoVisitor visitor, Object value) {
		return visitor.visitObject(this, value);
	}
	
	public Object accept(GoTruffle visitor, Object value) {
		return visitor.visitObject(this, value);
	}
	
	public String toString() {
		return name;
	}
}