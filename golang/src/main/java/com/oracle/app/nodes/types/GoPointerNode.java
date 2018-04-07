package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class GoPointerNode extends GoExpressionNode{

	protected int ptr;
	protected FrameSlot obj;
	protected boolean star = false;

	public GoPointerNode(int ptr, FrameSlot obj){
		this.ptr = ptr;
		this.obj = obj;
	}

	public static GoPointerNode createPointer(FrameSlot obj, FrameSlotKind type){
		switch(type){
		case Boolean:
			break;
		case Byte:
			break;
		case Double:
			break;
		case Float:
			break;
		case Illegal:
			break;
		case Int:
			return new GoIntPointerNode(obj.hashCode(),obj);
		case Long:
			break;
		case Object:
			break;
		default:
			break;
		
		}
		return null;
	}
	
	@Override
	public String toString(){
		return String.format("0x%x", ptr);
	}
	
	public abstract Object executeStar(VirtualFrame frame);
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return String.format("0x%x", ptr);
	}
	 
	public FrameSlot getSlot() {
		return obj;
	}

	public static class GoIntPointerNode extends GoPointerNode{
		
		public GoIntPointerNode(int ptr, FrameSlot obj) {
			super(ptr, obj);
			// TODO Auto-generated constructor stub
		}
		
		@Specialization
		public int doInt(VirtualFrame frame){
			return FrameUtil.getIntSafe(frame, getSlot());
		}

		@Override
		public Object executeStar(VirtualFrame frame) {
			return FrameUtil.getIntSafe(frame, getSlot());
		}
	}
	
}
