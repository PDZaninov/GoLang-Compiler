package com.oracle.app.nodes.expression;

import com.oracle.app.GoException;
import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoNonPrimitiveType;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.Shape;

@NodeChild("type")
public abstract class GoCompositeLitNode extends GoExpressionNode {

	private GoArrayExprNode elts;
	
	public GoCompositeLitNode(GoArrayExprNode elts) {
		this.elts = elts;
	}
	
	protected abstract GoExpressionNode getType();
	
	@Specialization
	public Object executeNonPrimitive(VirtualFrame frame, GoNonPrimitiveType type){
		Object[] elements = elts.gatherResults(frame);
		return type.doCompositeLit(frame, elements);
	}
	
	@Specialization
	public Object executeStruct(VirtualFrame frame, Shape struct){
		Object[] elements = elts.gatherResults(frame);
		if(elements.length == 0){
			return struct.newInstance();
		}
		else{
			if(elements[0] instanceof GoKeyValueNode){
				DynamicObject newStruct = struct.newInstance();
				for(Object value : elements){
					if(!newStruct.set(((GoKeyValueNode) value).getKey(), ((GoKeyValueNode) value).getResult())){
						throw new GoException("unknown field \'"+((GoKeyValueNode) value).getKey()+"\' in struct literal of type "+ getType().getName());
					}
				}
				return newStruct;
			}
			else{
				if(elements.length != struct.getPropertyCount()){
					throw new GoException("too few values in struct initializer");
				}
				return struct.createFactory().newInstance(elements);
			}
		}
	}
}
