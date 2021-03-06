package com.oracle.app.nodes.local;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.FieldNode;
import com.oracle.app.nodes.types.GoArray.GoFloat32Array;
import com.oracle.app.nodes.types.GoArray.GoFloat64Array;
import com.oracle.app.nodes.types.GoArray.GoIntArray;
import com.oracle.app.nodes.types.GoArray.GoObjectArray;
import com.oracle.app.nodes.types.GoArray.GoStringArray;
import com.oracle.app.nodes.types.GoMap;
import com.oracle.app.nodes.types.GoSlice.GoFloat32Slice;
import com.oracle.app.nodes.types.GoSlice.GoFloat64Slice;
import com.oracle.app.nodes.types.GoSlice.GoIntSlice;
import com.oracle.app.nodes.types.GoSlice.GoObjectSlice;
import com.oracle.app.nodes.types.GoSlice.GoStringSlice;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChildren({@NodeChild(value = "index", type = GoExpressionNode.class),@NodeChild(value = "array", type = GoReadLocalVariableNode.class)})
public abstract class GoArrayReadNode extends GoExpressionNode {
	
	public abstract GoExpressionNode getIndex();
	
	protected abstract GoReadLocalVariableNode getArray();
	
	//LOL
	public FrameSlot getSlot(){
		return getArray().getSlot();
	}
	
	@Specialization
	protected int readInt(VirtualFrame frame, int index, GoIntArray array){
		return array.read(index);
	}
	
	@Specialization
	protected float readFloat(VirtualFrame frame, int index, GoFloat32Array array){
		return array.read(index);
	}
	
	@Specialization
	protected double readDouble(VirtualFrame frame, int index, GoFloat64Array array){
		return array.read(index);
	}
	
	@Specialization
	protected String readString(VirtualFrame frame, int index, GoStringArray array){
		return array.read(index);
	}
	
	@Specialization
	protected Object readObject(VirtualFrame frame, int index, GoObjectArray array){
		return array.read(index);
	}
	
	@Specialization
	protected int readInt(VirtualFrame frame, int index, GoIntSlice array){
		return array.read(index);
	}
	
	@Specialization
	protected float readFloat(VirtualFrame frame, int index, GoFloat32Slice array){
		return array.read(index);
	}
	
	@Specialization
	protected double readDouble(VirtualFrame frame, int index, GoFloat64Slice array){
		return array.read(index);
	}
	
	@Specialization
	protected String readString(VirtualFrame frame, int index, GoStringSlice array){
		return array.read(index);
	}
	
	@Specialization
	protected Object readObject(VirtualFrame frame, int index, GoObjectSlice array){
		return array.read(index);
	}
	
	@Specialization
	protected Object readMap(VirtualFrame frame, Object key, GoMap map){
		return map.read(new FieldNode(key,null));
	}

}
