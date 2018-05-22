package com.oracle.app.nodes.local;

import com.oracle.app.GoException;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.Property;
import com.oracle.truffle.api.object.Shape;
import com.oracle.truffle.api.object.Shape.Allocator;

public abstract class GoWriteProperty extends Node {

	protected boolean createProperty;
	
	public GoWriteProperty(boolean createProperty){
		this.createProperty = createProperty;
	}
	
	public abstract Object executeWrite(Object receiver, Object name, Object value);
	
	@Specialization(guards = "isPropertyWrite()")
	public static Object writeExistingProperty(DynamicObject receiver, Object name, Object value){
		if(!receiver.set(name, value)){
			throw new GoException("undefined type, no field or method "+name);
		}
		return receiver;
	}
	
	//Property writes only occur for Struct function writes currently.
	//TODO Possibly set a flag when defining a property. And maybe use cache to retrieve the shape and location
	@Specialization
	public static Object writeNewProperty(Shape oldShape, Object name, Object value,
			@Cached("getAllocator(oldShape)") Allocator allocator){
		Property property = Property.create(name,allocator.declaredLocation(value),1);
		Shape newShape = oldShape.addProperty(property);
		return newShape;
	}
	
	protected Allocator getAllocator(Shape shape){
		return shape.allocator();
	}
	
	protected boolean isPropertyWrite(){
		return !createProperty;
	}
	
}
