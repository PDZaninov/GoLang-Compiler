package com.oracle.app.builtins;

import com.oracle.app.nodes.types.GoIntArray;
import com.oracle.app.nodes.types.GoIntSlice;
import com.oracle.app.nodes.types.GoSlice;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName="make")
public abstract class GoMakeBuiltin extends GoBuiltinNode {

	@Specialization
	public GoSlice make(GoIntArray array, int size){
		GoSlice slice = new GoIntSlice(size);
		return slice;
	}
	/*
	@Specialization
	public GoSlice make(GoIntArray array, int size, int capacity){
		GoSlice slice = new GoIntSlice(size, capacity);
		return slice;
		
	}

	@Specialization
	public GoMapNode make(Object map){
		return null;
		
	}
	
	@Specialization
	public GoMapNode make(Object map, int space){
		return null;
	} */
}
