package com.oracle.app.nodes.local;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.Shape;

@NodeField(name = "fieldName", type = String.class)
@NodeChildren({@NodeChild("receiver"),@NodeChild("value")})
public abstract class GoStructPropertyWrite extends GoExpressionNode {

	@Child private GoWriteProperty writeNode;
	
	public GoStructPropertyWrite(boolean createNewProperty){
		this.writeNode = GoWritePropertyNodeGen.create(createNewProperty);
	}
	
	public abstract String getFieldName();
	
	public abstract GoExpressionNode getReceiver();
	
	@Specialization
	public Object doWrite(VirtualFrame frame, DynamicObject receiver, Object value){
		writeNode.executeWrite(receiver, getFieldName(), value);
		return value;
	}
	
	@Specialization
	public Object newPropertyWrite(VirtualFrame frame, Shape receiver, Object value){
		frame.setObject(((GoReadLocalVariableNode) getReceiver()).getSlot(),writeNode.executeWrite(receiver, getFieldName(), value));
		return value;
	}
	
}
