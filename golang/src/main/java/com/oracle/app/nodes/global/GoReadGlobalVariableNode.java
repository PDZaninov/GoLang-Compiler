package com.oracle.app.nodes.global;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class GoReadGlobalVariableNode extends GoExpressionNode {
	
	MaterializedFrame globalFrame = GoLanguage.getCurrentContext().getGlobalFrame();

    @Override
	public String toString() {
		return "GoReadGlobalVariableNode [ "+getSlot()+" ]";
	}
    
    @Override
    public int hashCode(){
    	return getSlot().hashCode();
    }

    //Called by GoUnaryAddress to allow pointers to reference a frameslot...
	public abstract FrameSlot getSlot();
    
    @Specialization(guards = "isInt(frame)")
    protected int readInt(VirtualFrame frame){
    	return FrameUtil.getIntSafe(globalFrame, getSlot());
    }
    
    @Specialization(guards = "isFloat(frame)")
    protected float readFloat(VirtualFrame frame){
    	return FrameUtil.getFloatSafe(globalFrame, getSlot());
    }

    @Specialization(guards = "isDouble(frame)")
    protected double readDouble(VirtualFrame frame) { 
    	return FrameUtil.getDoubleSafe(globalFrame, getSlot());
    }

    @Specialization(guards = "isLong(frame)")
    protected long readLong(VirtualFrame frame) {
        return FrameUtil.getLongSafe(globalFrame, getSlot());
    }

    @Specialization(guards = "isBoolean(frame)")
    protected boolean readBoolean(VirtualFrame frame) {
        return FrameUtil.getBooleanSafe(globalFrame, getSlot());
    }
    
    @Specialization(guards = "isArray(frame)")
    protected Object readArray(VirtualFrame frame) {
    	
        return FrameUtil.getObjectSafe(globalFrame, getSlot());
    }
    
    @Specialization(guards = "isSlice(frame)")
    protected Object readSlice(VirtualFrame frame) {
    	
        return FrameUtil.getObjectSafe(globalFrame, getSlot());
    }
    
    @Specialization(guards = "isString(frame)")
    protected Object readString(VirtualFrame frame) {
        return FrameUtil.getObjectSafe(globalFrame, getSlot());
    }

    @Specialization(replaces = {"readInt", "readFloat","readDouble", "readLong", "readBoolean", "readArray", "readSlice", "readString"})
    protected Object readObject(VirtualFrame frame) {
        if (!globalFrame.isObject(getSlot())) {
            CompilerDirectives.transferToInterpreter();
            Object result = globalFrame.getValue(getSlot());
            globalFrame.setObject(getSlot(), result);	
            return result;
        }

        return FrameUtil.getObjectSafe(globalFrame, getSlot());
    }
    
    protected boolean isInt(VirtualFrame frame){
    	return getSlot().getKind() == FrameSlotKind.Int;
    }
    
    protected boolean isFloat(VirtualFrame frame){
    	return getSlot().getKind() == FrameSlotKind.Float;
    }

    protected boolean isDouble(VirtualFrame frame) { 
    	return getSlot().getKind() == FrameSlotKind.Double;
    }
    
    protected boolean isLong(VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Long;
    }

    protected boolean isBoolean(VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Boolean;
    }
    
    protected boolean isString( VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Object;
    }
    
    protected boolean isArray( VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Object;
    }
    
    protected boolean isSlice( VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Object;
    }
   
}