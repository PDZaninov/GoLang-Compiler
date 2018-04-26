package com.oracle.app.nodes;

import java.math.BigInteger;

import com.oracle.app.nodes.types.GoArray;
import com.oracle.app.nodes.types.GoPointerNode;
import com.oracle.app.nodes.types.GoMap;
import com.oracle.app.nodes.types.GoSlice;
import com.oracle.app.nodes.types.GoStruct;
import com.oracle.app.runtime.GoFunction;
import com.oracle.app.runtime.GoNull;
import com.oracle.truffle.api.dsl.TypeSystem;

@TypeSystem({
	int.class,
	float.class,
	double.class,
	long.class,
	BigInteger.class,
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
}