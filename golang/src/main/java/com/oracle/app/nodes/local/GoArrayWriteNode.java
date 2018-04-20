package com.oracle.app.nodes.local;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoArray;
import com.oracle.app.nodes.types.GoArray.GoFloat32Array;
import com.oracle.app.nodes.types.GoArray.GoFloat64Array;
import com.oracle.app.nodes.types.GoArray.GoIntArray;
import com.oracle.app.nodes.types.GoArray.GoObjectArray;
import com.oracle.app.nodes.types.GoArray.GoStringArray;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChildren({ 
	@NodeChild(value = "index",type = GoExpressionNode.class), 
	@NodeChild(value = "value", type = GoExpressionNode.class),
	@NodeChild(value = "array", type = GoReadLocalVariableNode.class)
	})
public abstract class GoArrayWriteNode extends GoExpressionNode {

	    @Specialization
	    public GoArray writeIntArray(VirtualFrame frame, int index, int value, GoIntArray array){
	    	array.insert(index, value);
	    	return null;
	    }
	    	
	    @Specialization
	    public GoArray writeFloatArray(VirtualFrame frame, int index, float value, GoFloat32Array array){
	    	array.insert(index, value);
	    	return null;
	    }
	    
	    @Specialization
	    public GoArray writeDoubleArray(VirtualFrame frame, int index, double value, GoFloat64Array array){
	    	array.insert(index, value);
	    	return null;
	    }
	    
	    @Specialization
	    public GoArray writeStringArray(VirtualFrame frame, int index, String value, GoStringArray array){
	    	array.insert(index, value);
	    	return null;
	    }
	    
	    @Specialization
	    public GoArray writeObjectArray(VirtualFrame frame, int index, Object value, GoObjectArray array){
	    	array.insert(index, value);
	    	return null;
	    }
}
