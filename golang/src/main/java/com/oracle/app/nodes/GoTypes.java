package com.oracle.app.nodes;

import com.oracle.app.nodes.types.GoArray;
import com.oracle.app.nodes.types.GoMap;
import com.oracle.app.nodes.types.GoPointerNode;
import com.oracle.app.nodes.types.GoSlice;
import com.oracle.app.nodes.types.GoStruct;
import com.oracle.app.runtime.GoFunction;
import com.oracle.app.runtime.GoNull;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.TypeSystem;

@TypeSystem({
	int.class,
	float.class,
	double.class,
	boolean.class,
	GoArray.class,
	GoSlice.class,
	GoMap.class,
	String.class,
	GoFunction.class,
	GoPointerNode.class,
	GoStruct.class,
	GoNull.class})
public abstract class GoTypes {
	
	@ImplicitCast
	@TruffleBoundary
	public static float castFloat(int value){
		return (float)value;
	}
	
	@ImplicitCast
	@TruffleBoundary
	public static double castDouble(int value){
		return (double)value;
	}
	
	@ImplicitCast
	@TruffleBoundary
	public static double castDouble(float value){
		return (double)value;
	}
	
}