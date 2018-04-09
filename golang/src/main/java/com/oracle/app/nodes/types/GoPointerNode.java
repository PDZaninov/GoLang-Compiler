package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.local.GoWriteMemoryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Truffle Pointer node. 
 * Holds a ptr for a makeshift address value
 * Holds a {@link FrameSlot} as a makeshift pointer value.
 * When executed it will return the value to read in the frameslot
 * Printing the toString of the pointer will return the makeshift ptr address
 * Dereferencing the address is handled in {@link GoWriteMemoryNode}
 * TO-DO - add PrimitiveType field to identify the pointer type
 * @author Trevor
 *
 */
public abstract class GoPointerNode extends GoExpressionNode{

	protected int ptr;
	protected FrameSlot obj;
	protected GoPrimitiveTypes type;

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
			return new GoObjectPointerNode(obj.hashCode(),obj);
		case Int:
			return new GoIntPointerNode(obj.hashCode(),obj);
		case Long:
			break;
		case Object:
			return new GoObjectPointerNode(obj.hashCode(),obj);
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
	
	public static class GoObjectPointerNode extends GoPointerNode{
		
		public GoObjectPointerNode(int ptr, FrameSlot obj) {
			super(ptr, obj);
			// TODO Auto-generated constructor stub
		}
		
		@Specialization
		public Object doObject(VirtualFrame frame){
			return FrameUtil.getObjectSafe(frame, getSlot());
		}

		@Override
		public Object executeStar(VirtualFrame frame) {
			return FrameUtil.getObjectSafe(frame, getSlot());
		}
	}
	
}
