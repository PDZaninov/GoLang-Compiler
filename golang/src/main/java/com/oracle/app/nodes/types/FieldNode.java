package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.expression.GoKeyValueNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class FieldNode extends GoExpressionNode{
    protected Object value;
    protected String type;

    public FieldNode(Object value, String type){
        this.value = value;
        this.type = type;
    }

    public void insert(GoKeyValueNode value){
    	
    }
    
    public void insert(Object value){
        this.value = value;
    }

    public Object read(){
        return this.value;
    }

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		// TODO Auto-generated method stub
		return null;
	}

}