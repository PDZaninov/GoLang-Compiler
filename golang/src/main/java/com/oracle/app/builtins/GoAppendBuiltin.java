package com.oracle.app.builtins;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoArray.GoFloat32Array;
import com.oracle.app.nodes.types.GoArray.GoFloat64Array;
import com.oracle.app.nodes.types.GoArray.GoIntArray;
import com.oracle.app.nodes.types.GoArray.GoObjectArray;
import com.oracle.app.nodes.types.GoArray.GoStringArray;
import com.oracle.app.nodes.types.GoPrimitiveTypes;
import com.oracle.app.nodes.types.GoSlice;
import com.oracle.app.nodes.types.GoSlice.GoFloat32Slice;
import com.oracle.app.nodes.types.GoSlice.GoFloat64Slice;
import com.oracle.app.nodes.types.GoSlice.GoIntSlice;
import com.oracle.app.nodes.types.GoSlice.GoObjectSlice;
import com.oracle.app.nodes.types.GoSlice.GoStringSlice;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "append")
public class GoAppendBuiltin extends GoExpressionNode {

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		if(frame.getArguments().length < 1){
			return null;
		}
		Object[] arguments = frame.getArguments();
		GoSlice originalslice = (GoSlice) arguments[0];
		GoSlice slice = createSliceCopy(originalslice.getType());
		for(int i = 0; i < arguments.length; i++){
			if(arguments[i] instanceof GoSlice){
				GoSlice appendSlice = (GoSlice) arguments[i];
				for(int j = 0; j < appendSlice.len(); j++){
					slice.insert(appendSlice.read(j));
				}
			}
			else{
				slice.insert(arguments[i]);
			}
		}
		return slice;
	}
	
	private GoSlice createSliceCopy(GoPrimitiveTypes type) {
		switch(type){
		case FLOAT32:
			return new GoFloat32Slice(new GoFloat32Array(0),0,0,0);
		case FLOAT64:
			return new GoFloat64Slice(new GoFloat64Array(0),0,0,0);
		case INT:
			return new GoIntSlice(new GoIntArray(0),0,0,0);
		case OBJECT:
			return new GoObjectSlice(new GoObjectArray(0),0,0,0);
		case STRING:
			return new GoStringSlice(new GoStringArray(0),0,0,0);
		case BOOL:
		}
		return null;
	}

	public static GoAppendBuiltin getAppendBuiltin(){
		return new GoAppendBuiltin();
	}

}
