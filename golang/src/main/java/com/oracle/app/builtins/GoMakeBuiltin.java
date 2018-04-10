package com.oracle.app.builtins;

import com.oracle.app.nodes.types.GoArray;
import com.oracle.app.nodes.types.GoSlice;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName="make")
public abstract class GoMakeBuiltin extends GoBuiltinNode {
	
	@Specialization
	public GoSlice make(GoArray array, int size, int capacity){
		return null;
	}
}
