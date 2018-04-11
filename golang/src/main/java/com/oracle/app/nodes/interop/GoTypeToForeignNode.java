package com.oracle.app.nodes.interop;

import com.oracle.app.nodes.GoTypes;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;

@TypeSystemReference(GoTypes.class)
public abstract class GoTypeToForeignNode extends Node {
	
	public abstract Object executeConvert(Object value);
	
	@Specialization
	static long fromLong(long value){
		return value;
	}
	
	@Fallback
	static Object identity(Object value){
		return value;
	}
}
