package com.oracle.app.nodes.local;

import com.oracle.app.GoException;
import com.oracle.app.runtime.GoContext;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.FinalLocationException;
import com.oracle.truffle.api.object.IncompatibleLocationException;
import com.oracle.truffle.api.object.Location;
import com.oracle.truffle.api.object.Property;
import com.oracle.truffle.api.object.Shape;

public abstract class GoWriteProperty extends Node {

	public abstract void executeWrite(Object receiver, Object name, Object value);

	@Specialization
	public static void writeExistingProperty(DynamicObject receiver, Object name, Object value){
		if(!receiver.set(name, value)){
			throw new GoException("undefined type, no field or method "+name);
		}
	}
	
}
