package com.oracle.app.builtins;

import com.oracle.app.nodes.types.GoArray;
import com.oracle.app.nodes.types.GoMapNode;
import com.oracle.app.nodes.types.GoSliceNode;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName="make")
public abstract class GoMakeBuiltin extends GoBuiltinNode {

	public GoSliceNode make(GoArray array, int size){
		
		return null;
		
	}
	
	public GoSliceNode make(GoArray array, int size, int capacity){
		return null;
		
	}

	public GoMapNode make(Object map){
		return null;
		
	}
	
	public GoMapNode make(Object map, int space){
		return null;
	}
}
