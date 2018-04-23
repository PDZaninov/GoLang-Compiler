package com.oracle.app.nodes.local;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoPointerNode.GoArrayIndexPointerNode;
import com.oracle.app.nodes.types.GoPointerNode.GoIntPointerNode;
import com.oracle.app.nodes.types.GoPointerNode.GoObjectPointerNode;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Handles all pointer writes and mimics a memory write by writing directly to the referenced {@link FrameSlot}
 * Is essentially structured the same as {@link GoReadLocalVariableNode} except
 * it needs to extract the pointer object first, then set the value in the slot the pointer points at.
 * @author Trevor
 *
 */
@NodeChild("valueNode")
@NodeChild(value = "pointee", type = GoReadLocalVariableNode.class)
public abstract class GoWriteMemoryNode extends GoExpressionNode {
	
	@Specialization
	protected int writeInt(VirtualFrame frame, int value, GoIntPointerNode ptr){
        frame.setInt(ptr.getSlot(), value);
        return value;
	}
	
	@Specialization
	protected Object writeArrayIndex(VirtualFrame frame, Object value, GoArrayIndexPointerNode ptr){
		ptr.insert(value);
		return null;
	}
	
	@Specialization
	protected Object write(VirtualFrame frame, Object value, GoObjectPointerNode ptr){
		frame.setObject(ptr.getSlot(), value);
		return value;
	}

}
