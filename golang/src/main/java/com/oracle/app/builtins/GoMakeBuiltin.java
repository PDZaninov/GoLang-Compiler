package com.oracle.app.builtins;

import com.oracle.app.GoException;
import com.oracle.app.nodes.types.GoArray;
import com.oracle.app.nodes.types.GoSlice;
import com.oracle.app.runtime.GoNull;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "make")
public abstract class GoMakeBuiltin extends GoBuiltinNode{
	
	@Specialization
	public GoSlice makeSlice(GoSlice slice, int len, int cap){
		if(len > cap){
			throw new GoException("len larger than cap in make()");
		}
		return slice.make(len, cap);
	}
	
	@Specialization
	public GoSlice makeSlice(GoSlice slice, int len, GoNull x){
		return slice.make(len, len);
	}
	
	//Saved off for Maps
	@Specialization
	public GoSlice makeSlice(GoArray array, int len, GoNull x){
		System.out.println("Not the right thing");
		return null;
	}
	
	@Specialization
	public GoSlice makeSlice(GoArray array, GoNull x, GoNull y){
		System.out.println("Not good again");
		return null;
	}
	
}
