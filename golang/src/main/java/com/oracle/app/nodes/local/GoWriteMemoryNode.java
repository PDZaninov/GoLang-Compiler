package com.oracle.app.nodes.local;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoPointerNode;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Handles all pointer writes and mimics a memory write by writing directly to the referenced {@link FrameSlot}
 * Is essentially structured the same as {@link GoReadLocalVariableNode} except
 * it needs to extract the pointer object first, then set the value in the slot the pointer points at.
 * @author Trevor
 *
 */
@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class GoWriteMemoryNode extends GoExpressionNode {

	protected abstract FrameSlot getSlot();
	
	@Specialization
	protected int writeInt(VirtualFrame frame, int value){
		getSlot().setKind(FrameSlotKind.Int);
        GoPointerNode ptr = (GoPointerNode) FrameUtil.getObjectSafe(frame, getSlot());
        frame.setInt(ptr.getSlot(), value);
        return value;
	}
	
	@Specialization
	protected String writeString(VirtualFrame frame, String value){
		getSlot().setKind(FrameSlotKind.Object);
		GoPointerNode ptr = (GoPointerNode) FrameUtil.getObjectSafe(frame, getSlot());
		frame.setObject(ptr.getSlot(), value);
		return value;
	}
	
	@Specialization
	protected Object write(VirtualFrame frame, Object value){
		getSlot().setKind(FrameSlotKind.Object);
		GoPointerNode ptr = (GoPointerNode) FrameUtil.getObjectSafe(frame, getSlot());
		frame.setObject(ptr.getSlot(), value);
		return value;
	}

}
