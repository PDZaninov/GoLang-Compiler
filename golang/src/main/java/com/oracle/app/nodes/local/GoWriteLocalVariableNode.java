package com.oracle.app.nodes.local;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;


@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class GoWriteLocalVariableNode  extends GoExpressionNode{
	
	    protected abstract FrameSlot getSlot();
	    
	    @Specialization(guards = "isIntOrIllegal(frame)")
	    protected int writeInt(VirtualFrame frame, int value) {

	        getSlot().setKind(FrameSlotKind.Int);

	        frame.setInt(getSlot(), value);
	        return value;
	    }
	    
	    @Specialization(guards = "isFloatOrIllegal(frame)")
	    protected float writeFloat(VirtualFrame frame, float value) {
	        /* Initialize type on first write of the local variable. No-op if kind is already Long. */
	        getSlot().setKind(FrameSlotKind.Float);

	        frame.setFloat(getSlot(), value);
	        return value;
	    }

	    @Specialization(guards = "isLongOrIllegal(frame)")
	    protected long writeLong(VirtualFrame frame, long value) {

	        getSlot().setKind(FrameSlotKind.Long);

	        frame.setLong(getSlot(), value);
	        return value;
	    }

	    @Specialization(guards = "isBooleanOrIllegal(frame)")
	    protected boolean writeBoolean(VirtualFrame frame, boolean value) {
	        getSlot().setKind(FrameSlotKind.Boolean);

	        frame.setBoolean(getSlot(), value);
	        return value;
	    }
	    
	    @Specialization(guards = "isStringOrIllegal(frame)")
	    protected String writeString(VirtualFrame frame, String value) {
	        /* Initialize type on first write of the local variable. No-op if kind is already Long. */
	        getSlot().setKind(FrameSlotKind.Object);

	        frame.setObject(getSlot(), value);
	        return value;
	    }


	    @Specialization(replaces = {"writeInt", "writeFloat", "writeLong", "writeBoolean", "writeString"})
	    protected Object write(VirtualFrame frame, Object value) {

	        getSlot().setKind(FrameSlotKind.Object);

	        frame.setObject(getSlot(), value);
	        return value;
	    }
	    
	    protected boolean isIntOrIllegal(VirtualFrame frame) {
	        return getSlot().getKind() == FrameSlotKind.Int || getSlot().getKind() == FrameSlotKind.Illegal;
	    }
	    
	    protected boolean isFloatOrIllegal(VirtualFrame frame) {
	        return getSlot().getKind() == FrameSlotKind.Float || getSlot().getKind() == FrameSlotKind.Illegal;
	    }
	    
	    protected boolean isLongOrIllegal(VirtualFrame frame) {
	        return getSlot().getKind() == FrameSlotKind.Long || getSlot().getKind() == FrameSlotKind.Illegal;
	    }

	    protected boolean isBooleanOrIllegal(@SuppressWarnings("unused") VirtualFrame frame) {
	        return getSlot().getKind() == FrameSlotKind.Boolean || getSlot().getKind() == FrameSlotKind.Illegal;
	    }
	    
	    protected boolean isStringOrIllegal(@SuppressWarnings("unused") VirtualFrame frame) {
	        return getSlot().getKind() == FrameSlotKind.Object || getSlot().getKind() == FrameSlotKind.Illegal;
	    }
}
