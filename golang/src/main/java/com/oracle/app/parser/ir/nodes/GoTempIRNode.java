package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitable;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoTempIRNode extends GoBaseIRNode {

	Map<String, String> attrs = new HashMap<>();
	public ArrayList<GoBaseIRNode> children;
	
	public GoTempIRNode(String name, Map<String, String> attributes, ArrayList<GoBaseIRNode> childs) {
		super(name);

		attrs = attributes;
		children = childs;
		setChildParent();
	}

	@Override
	public void setChildParent() {
		if(children.isEmpty())
		for(int x =0; x < children.size();x++) {
			children.get(x).setParent(this);
		}
		
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		return children;
		
	}
	
	@Override
	public void accept(GoIRVisitor visitor) { 
		visitor.visitObject(this); 
	}

}
