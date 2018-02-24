package com.oracle.app.parser.ir;

import java.util.ArrayList;

public abstract class GoBaseIRNode {
	
	protected String name;

    protected GoBaseIRNode parent;
	
	public GoBaseIRNode(String name) {
		this.name = name;
	}
	
	public abstract void setChildParent();
	
	public abstract ArrayList<GoBaseIRNode> getChildren();
	
	public GoBaseIRNode getParent() {
		return parent;
	}
	
	public void setParent(GoBaseIRNode node) {
		this.parent = node;
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