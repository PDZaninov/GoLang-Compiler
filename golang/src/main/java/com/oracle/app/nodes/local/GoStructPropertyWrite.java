package com.oracle.app.nodes.local;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.object.DynamicObject;

@NodeChildren({@NodeChild("receiver"),@NodeChild("name"),@NodeChild("value")})
public abstract class GoStructPropertyWrite extends GoExpressionNode {

	@Child private GoWriteProperty writeNode = GoWritePropertyNodeGen.create();
	
	@Specialization
	public Object doWrite(DynamicObject receiver, Object name, Object value){
		writeNode.executeWrite(receiver, name, value);
		return value;
	}
	
}
