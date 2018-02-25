package com.oracle.app.parser.ir;

import java.util.ArrayList;
import java.util.Map;

public abstract class GoBaseIRNode implements GoIRVisitable {
	
	protected String name;

    protected GoBaseIRNode parent;
	
	public GoBaseIRNode(String name) { this.name = name; }
	
	public abstract void setChildParent();
	
	public abstract ArrayList<GoBaseIRNode> getChildren();
	
	public GoBaseIRNode getParent() { return parent; }
	
	public void setParent(GoBaseIRNode node) { this.parent = node; }
	
	public abstract Object accept(GoIRVisitor visitor);
	
	public String toString() { return name; }
}