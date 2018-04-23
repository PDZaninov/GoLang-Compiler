package com.oracle.app.nodes.types;

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
 * @author Trevor
 *
 */
public abstract class GoPointerNode extends GoNonPrimitiveType{

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
			return new GoBooleanPointerNode(obj.hashCode(), obj);
		case Double:
			return new GoFloat64PointerNode(obj.hashCode(), obj);
		case Float:
			return new GoFloat32PointerNode(obj.hashCode(), obj);
		case Illegal:
			return new GoObjectPointerNode(obj.hashCode(),obj);
		case Int:
			return new GoIntPointerNode(obj.hashCode(),obj);
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
		return this.copy();
	}
	 
	public FrameSlot getSlot() {
		return obj;
	}
 
	@Override
	public GoNonPrimitiveType doCompositeLit(VirtualFrame frame, Object[] results){
		return null;
	}
	
	public static class GoArrayIndexPointerNode extends GoPointerNode{
		
		GoArrayLikeTypes array;
		Object index;
		
		public GoArrayIndexPointerNode(int ptr, GoArrayLikeTypes array, Object index){
			super(ptr,null);
			this.array = array;
			this.index = index;
		}

		@Override
		public Object executeStar(VirtualFrame frame) {
			return array.read(index);
		}

		public void insert(Object value) {
			array.insert(index,value);
		}
	}
	
	public static class GoIntPointerNode extends GoPointerNode{
		
		public GoIntPointerNode(int ptr, FrameSlot obj) {
			super(ptr, obj);
		}
		
		@Specialization
		public int doInt(VirtualFrame frame){
			return FrameUtil.getIntSafe(frame, getSlot());
		}

		@Override
		public Object executeStar(VirtualFrame frame) {
			return doInt(frame);
		}
	}
	
	public static class GoFloat32PointerNode extends GoPointerNode{
		public GoFloat32PointerNode(int ptr, FrameSlot obj){
			super(ptr, obj);
		}
		
		@Specialization 
		public float doFloat(VirtualFrame frame){
			return FrameUtil.getFloatSafe(frame, getSlot());
		}
		
		@Override 
		public Object executeStar(VirtualFrame frame) {
			return doFloat(frame);
		}
		
	}
	
	public static class GoFloat64PointerNode extends GoPointerNode{
		public GoFloat64PointerNode(int ptr, FrameSlot obj){
			super(ptr, obj);
		}
		
		@Specialization 
		public double doDouble(VirtualFrame frame){
			return FrameUtil.getDoubleSafe(frame, getSlot());
		}
		
		@Override public Object executeStar(VirtualFrame frame) {
			return doDouble(frame);
		}
		
	}
	
	public static class GoBooleanPointerNode extends GoPointerNode{
		public GoBooleanPointerNode(int ptr, FrameSlot obj){
			super(ptr, obj);
		}
		
		@Specialization 
		public boolean doBool(VirtualFrame frame){
			return FrameUtil.getBooleanSafe(frame, getSlot());
		}
		
		@Override public Object executeStar(VirtualFrame frame) {
			return doBool(frame);
		}
		
	}
	
	public static class GoObjectPointerNode extends GoPointerNode{
		
		public GoObjectPointerNode(int ptr, FrameSlot obj) {
			super(ptr, obj);
		}
		
		@Specialization
		public Object doObject(VirtualFrame frame){
			return FrameUtil.getObjectSafe(frame, getSlot());
		}

		@Override
		public Object executeStar(VirtualFrame frame) {
			return doObject(frame);
		}
	}
	
}
