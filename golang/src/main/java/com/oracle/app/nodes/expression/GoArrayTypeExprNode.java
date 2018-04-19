package com.oracle.app.nodes.expression;

import java.util.Arrays;
import java.util.Random;

import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoArray;
import com.oracle.app.nodes.types.GoIntNode;
import com.oracle.app.nodes.types.GoPrimitiveTypes;
import com.oracle.app.nodes.types.GoSlice;
import com.oracle.app.runtime.GoNull;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoArrayTypeExprNode extends GoExpressionNode {

	private int length;
	private GoPrimitiveTypes type;
	private GoExpressionNode typenode;
	
	public GoArrayTypeExprNode(GoIntNode length,GoExpressionNode typenode){
		this.length = length.executeInteger(null);
		this.typenode = typenode;
	}
	//Needs to execute the element type expression first
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		//CompilerDirectives.transferToInterpreter();
		//GoArray result = new GoArray(length, type);
		Object[] temp = new Object[length];
		Object result = typenode.executeGeneric(frame);
		Arrays.fill(temp, result);
		if(result instanceof Integer){
			this.type = GoPrimitiveTypes.INT;
		}
		else if(result instanceof Float){
			this.type = GoPrimitiveTypes.FLOAT32;
		}
		else if(result instanceof Double){
			this.type = GoPrimitiveTypes.FLOAT64;
		}
		else if(result instanceof String){
			this.type = GoPrimitiveTypes.STRING;
		}
		else{
			this.type = GoPrimitiveTypes.OBJECT;
			//System.out.println("Array Type "+type+" not implemented");
		}
		return fillCompositeFields(frame, temp);

	}
	
	public Object fillCompositeFields(VirtualFrame frame, GoArrayExprNode elts){
		Object[] results = elts.gatherResults(frame);
		if(results[0] instanceof Integer){
			this.type = GoPrimitiveTypes.INT;
		}
		else if(results[0] instanceof Float){
			this.type = GoPrimitiveTypes.FLOAT32;
		}
		else if(results[0] instanceof Double){
			this.type = GoPrimitiveTypes.FLOAT64;
		}
		else if(results[0] instanceof String){
			this.type = GoPrimitiveTypes.STRING;
		}
		else{
			this.type = GoPrimitiveTypes.OBJECT;
			//System.out.println("Array Type "+type+" not implemented");
		}
		return fillCompositeFields(frame, results);
	}
	/**
	 * Creates an arraylike type object and returns it.
	 * Bug - Currently cannot create 2D arrays or bigger with a composite lit
	 * @param frame
	 * @param results - Array of values to initialize the array with
	 * @return - A GoArray object or a Slice if the size was zero
	 */
	public Object fillCompositeFields(VirtualFrame frame, Object[] results) {
		boolean slice = false;
		//Filling an array that was initially empty. Might be source of slow stuff
		if(length == 0){	
			length = results.length;
			slice = true;
		}
		FrameSlot[] arr = new FrameSlot[length];
		CompilerDirectives.transferToInterpreter();
		Random rand = new Random();
		int hash = hashCode() + rand.nextInt();
		FrameSlot indexSlot;
		String temporaryIdentifier;
		FrameDescriptor frameDescriptor = frame.getFrameDescriptor();
		for(int i = 0; i < length; i++){
			temporaryIdentifier = String.format("_0x%x_%d", hash,i);
			indexSlot = frameDescriptor.addFrameSlot(temporaryIdentifier);
			arr[i] = indexSlot;
		}
		
		int i = 0;
		switch(type){
		case BOOL:
			for(; i < results.length; i++){
				frame.setBoolean(arr[i], (boolean) results[i]);
			}
			for(;i < arr.length; i++){
				arr[i].setKind(FrameSlotKind.Boolean);
				frame.setBoolean(arr[i], false);
			}
			break;
		case FLOAT64:
			for(; i < results.length; i++){
				frame.setFloat(arr[i], (float) results[i]);
			}
			for(; i < arr.length; i++){
				arr[i].setKind(FrameSlotKind.Float);
				frame.setFloat(arr[i], (float) 0);
			}
			break;
		case INT:
			for(; i < results.length; i++){
				frame.setInt(arr[i], (int) results[i]);
			}
			for(; i < arr.length; i++){
				arr[i].setKind(FrameSlotKind.Int);
				frame.setInt(arr[i], (int) 0);
			}
			break;
		case STRING:
			for(; i < results.length; i++){
				frame.setObject(arr[i], results[i]);
			}
			for(; i < arr.length; i++){
				arr[i].setKind(FrameSlotKind.Object);
				frame.setObject(arr[i], "");
			}
			break;
		case OBJECT:
			for(; i < results.length; i++){
				frame.setObject(arr[i], results[i]);
			}
			for(; i < arr.length; i++){
				arr[i].setKind(FrameSlotKind.Object);
				frame.setObject(arr[i], GoNull.SINGLETON);
			}
			break;
		default:
			break;
		
		}
		GoArray result = new GoArray(length,type,arr);
		if(slice){
			FrameSlot slot = frameDescriptor.addFrameSlot(String.format("_0x%x", hashCode()));
			frame.setObject(slot, result);
			return new GoSlice(slot, 0, length, length,type);
		}
		return result;
	}

}
