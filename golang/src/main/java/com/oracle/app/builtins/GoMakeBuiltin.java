package com.oracle.app.builtins;

import com.oracle.app.GoException;
import com.oracle.app.nodes.types.GoArray;
import com.oracle.app.nodes.types.GoMap;
import com.oracle.app.nodes.types.GoSlice;
import com.oracle.app.runtime.GoNull;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

/**
 * Make builtin only for slices and maps. Missing channels because we do not implement channel types
 * Maps are made before make is called so just need to return the object itself.
 * Maps made with a capacity should only be called on new maps, but has the possibility of being called on
 * maps that are already defined.
 * @author Trevor
 *
 */
@NodeInfo(shortName = "make")
public abstract class GoMakeBuiltin extends GoBuiltinNode{
	
	@Specialization
	public GoSlice makeSliceWithCapacity(GoSlice slice, int len, int cap){
		if(len > cap){
			throw new GoException("len larger than cap in make()");
		}
		return slice.make(len, cap);
	}
	
	@Specialization
	public GoSlice makeSlice(GoSlice slice, int len, GoNull x){
		return slice.make(len, len);
	}
	
	@Specialization
	public GoMap makeMapWithCapacity(GoMap map, int len, GoNull x){
		map.setNewMapCapacity(len);
		return map;
	}
	
	@Specialization
	public GoMap makeMap(GoMap map, GoNull x, GoNull y){
		return map;
	}
	
}
