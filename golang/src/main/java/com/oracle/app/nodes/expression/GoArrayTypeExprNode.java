package com.oracle.app.nodes.expression;

import java.util.Random;

import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoArray;
import com.oracle.app.nodes.types.GoIntNode;
import com.oracle.app.nodes.types.GoPrimitiveTypes;
import com.oracle.app.nodes.types.GoSlice;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoArrayTypeExprNode extends GoExpressionNode {

	private int length;
	private GoPrimitiveTypes type;
	
	public GoArrayTypeExprNode(GoIntNode length,String type){
		this.length = length.executeInteger(null);
		switch(type){
		case "int":
			this.type = GoPrimitiveTypes.INT;
			break;
		case "string":
			this.type = GoPrimitiveTypes.STRING;
			break;
		default:
			System.out.println("Array Type "+type+" not implemented");
		}
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		CompilerDirectives.transferToInterpreter();
		//GoArray result = new GoArray(length, type);
		Object[] temp = new Object[length];
		switch(type){
		case BOOL:
			for(int i = 0; i < length; i++){
				temp[i] = false;
			}
			return fillCompositeFields(frame, temp);
			
		case FLOAT64:
			for(int i = 0; i < length; i++){
				temp[i] = (float)0;
			}
			return fillCompositeFields(frame, temp);
			
		case INT:
			for(int i = 0; i < length; i++){
				temp[i] = 0;
			}
			return fillCompositeFields(frame, temp);
			
		case STRING:
			for(int i = 0; i < length; i++){
				temp[i] = "";
			}
			return fillCompositeFields(frame, temp);
			
		default:
			break;
		}
		return null;
	}
	
	public Object fillCompositeFields(VirtualFrame frame, GoArrayExprNode elts){
		Object[] results = elts.gatherResults(frame);
		return fillCompositeFields(frame, results);
	}
	/**
	 * Creates an arraylike type object and returns it.
	 * 
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
