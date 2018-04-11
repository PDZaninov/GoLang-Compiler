package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoTempIRNode extends GoBaseIRNode {

	Map<String, String> attrs = new HashMap<>();
	Map<String,GoBaseIRNode> children;
	GoBaseIRNode parent;
	
	public GoTempIRNode(String name, Map<String, String> attributes, Map<String, GoBaseIRNode> childs) {
		super(name);

		attrs = attributes;
		children = childs;
		setChildParent();
	}

	public void setParent(GoBaseIRNode node) { 
		this.parent = node; 
	}
	
	public void setChildParent() {
		if(children.isEmpty()){
			for(GoBaseIRNode child : children.values()){
				((GoTempIRNode) child).setParent(this);
			}
		}
		
	}

	public ArrayList<GoBaseIRNode> getChildren() {
		ArrayList<GoBaseIRNode> temp = new ArrayList<>();
		for(GoBaseIRNode child : children.values()){
			temp.add(child);
		}
		return temp;
		
	}
	
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitObject(this); 
	}

}
