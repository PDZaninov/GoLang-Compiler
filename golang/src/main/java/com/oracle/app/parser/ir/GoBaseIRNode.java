package com.oracle.app.parser.ir;

import java.util.ArrayList;

public abstract class GoBaseIRNode implements GoIRVisitable {
	
	protected String name;
    protected GoBaseIRNode parent;
	
	public GoBaseIRNode(String name) { this.name = name; }
	public void setParent(GoBaseIRNode node) { this.parent = node; }
	public String toString() { return name; }
	public GoBaseIRNode getParent() { return parent; }
	public String getIdentifier(){ return name; }
	
	public abstract void setChildParent();
	public abstract ArrayList<GoBaseIRNode> getChildren();
	public abstract Object accept(GoIRVisitor visitor);
	
	
}