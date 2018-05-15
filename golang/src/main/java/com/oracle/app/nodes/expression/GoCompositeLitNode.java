package com.oracle.app.nodes.expression;

import java.util.List;

import com.oracle.app.GoException;
import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoNonPrimitiveType;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.Property;
import com.oracle.truffle.api.object.Shape;

@NodeChild("type")
public abstract class GoCompositeLitNode extends GoExpressionNode {

	private GoArrayExprNode elts;
	private final int STRUCT_FIELD = 0;
	private final int STRUCT_METHOD = 1;
	
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
					//If the property is the function
					if(!struct.hasProperty(key) || struct.getProperty(key).getFlags() == STRUCT_METHOD){
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
				List<Object> keylist = struct.getKeyList(getKeyList());
				if(elements.length != keylist.size()){
					throw new GoException("too few values in struct initializer");
				}
				return struct.createFactory().newInstance(elements);
			}
		}
	}
	
	protected Shape.Pred<Property> getKeyList(){
		return p -> p.getFlags() == STRUCT_FIELD;
	}
}
