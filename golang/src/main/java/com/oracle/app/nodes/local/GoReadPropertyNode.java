package com.oracle.app.nodes.local;

import com.oracle.app.GoException;
import com.oracle.app.runtime.GoContext;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.object.DynamicObject;

public abstract class GoReadPropertyNode extends Node {

	public abstract Object executeRead(Object receiver, Object name);

	@Specialization(guards = "isStruct(receiver)")
	public Object doRead(DynamicObject receiver, Object name){
		Object result = receiver.get(name);
		if(result == null){
			throw new GoException("undefined field "+ name);
		}
		return result;
	}
	
	public boolean isStruct(DynamicObject receiver){
		CompilerAsserts.neverPartOfCompilation();
		if(!GoContext.isGoStruct(receiver)){
			return false;
		}
		return receiver.getShape().isValid();
	}
}
