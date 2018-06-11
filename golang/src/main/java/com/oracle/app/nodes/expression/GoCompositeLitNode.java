package com.oracle.app.nodes.expression;

import java.util.List;

import com.oracle.app.GoException;
import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoNonPrimitiveType;
import com.oracle.app.nodes.types.GoStruct;
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
	
	/**
	 * Struct objects can be initialized by keyvalue nodes or by listing out the fields in order.
	 * There cannot be a mix of the two so if a keyvaluenode shows up then the rest of the values have to be keyvaluenodes
	 */
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
					GoKeyValueNode unboxedval = (GoKeyValueNode) value;
					Object key = unboxedval.getKeyResult();
					//If the property is the method receiver, it technically should be not found, but truffle crashes.
					//It probably needs some location property read.
					if(!struct.hasProperty(key) || struct.getProperty(key).getFlags() == GoStruct.STRUCT_METHOD){
						throw new GoException("unknown field \'"+key+"\' in struct literal of type "+ getType().getName());
					}
					newStruct.set(key, unboxedval.getResult());
					/*
					if(!newStruct.set(((GoKeyValueNode) value).getKey().executeGeneric(frame), ((GoKeyValueNode) value).getResult())){
						throw new GoException("unknown field \'"+((GoKeyValueNode) value).getKey()+"\' in struct literal of type "+ getType().getName());
					}*/
				}
				return newStruct;
			}
			else{
				//Need to get a list of the struct field keys, struct methods are listed as properties in the shape.
				List<Object> keylist = struct.getKeyList(GoStruct.getKeyList());
				if(elements.length != keylist.size()){
					throw new GoException("too few values in struct initializer");
				}
				return struct.createFactory().newInstance(elements);
			}
		}
	}
}
