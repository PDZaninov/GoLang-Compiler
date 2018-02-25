package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitable;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoTempIRNode extends GoBaseIRNode {

	Map<String, String> attrs = new HashMap<>();
	public Map<String,GoBaseIRNode> children;
	
	public GoTempIRNode(String name, Map<String, String> attributes, Map<String, GoBaseIRNode> childs) {
		super(name);

		attrs = attributes;
		children = childs;
		setChildParent();
	}

	@Override
	public void setChildParent() {
		if(children.isEmpty()){
			for(GoBaseIRNode child : children.values()){
				child.setParent(this);
			}
		}
		
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		ArrayList<GoBaseIRNode> temp = new ArrayList<>();
		for(GoBaseIRNode child : children.values()){
			temp.add(child);
		}
		return temp;
		
	}
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitObject(this); 
	}

}
