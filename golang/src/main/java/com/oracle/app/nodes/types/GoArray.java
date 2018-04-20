package com.oracle.app.nodes.types;

import java.util.Arrays;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Arrays are initially empty and undefined arrays until executed. When executed
 * a specialized array type is created.
 * Missing boolean type array
 * When length = 0, a slice should be created
 * @author Trevor
 *
 */
public class GoArray extends GoArrayLikeTypes{
	protected GoIntNode lengthnode;
	protected int length;
	protected GoExpressionNode typeexpr;
	
	public GoArray(GoIntNode length, GoExpressionNode typeexpression){
		this.lengthnode = length;
		this.typeexpr = typeexpression;
	}
	
	@Override
	public GoPrimitiveTypes getType() {
		return null;
	}
	
	@Override
	public int len(){
		return length;
	}
	
	@Override
	public int cap(){
		return length;
	}
	
	@Override
	public int lowerBound(){
		return 0;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame){
		length = lengthnode.executeInteger(frame);
		Object kind = typeexpr.executeGeneric(frame);
		GoArray result = null;
		if(kind instanceof Integer){
			result = new GoIntArray(length);
		}
		else if(kind instanceof Float){
			result =  new GoFloat32Array(length);
		}
		else if(kind instanceof Double){
			result =  new GoFloat64Array(length);
		}
		else if(kind instanceof String){
			result =  new GoStringArray(length);
		}
		else{
			result =  new GoObjectArray(length);
		}
		return result;
	}
	
	public static class GoIntArray extends GoArray{

		private int[] array;
		
		public GoIntArray(int length) {
			super(null, null);
			this.length = length;
			array = new int[length];
		}
		
		@Override
		public GoPrimitiveTypes getType(){
			return GoPrimitiveTypes.INT;
		}
		
		@Override
		public String toString(){
			return Arrays.toString(array);
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.deepCopy();
		}
		
		public void insert(int index, int value){
			if(index < 0 || index > array.length){
				System.out.println("Index out of bounds");
			}
			array[index] = value;
		}
		
		public int read(int index){
			if(index < 0 || index > array.length){
				System.out.println("Index out of bounds");
			}
			return array[index];
		}
		
	}
	
	public static class GoFloat32Array extends GoArray{

		private float[] array;
		
		public GoFloat32Array(int length) {
			super(null, null);
			this.length = length;
			array = new float[length];
		}
		
		@Override
		public GoPrimitiveTypes getType(){
			return GoPrimitiveTypes.FLOAT32;
		}
		
		@Override
		public String toString(){
			return Arrays.toString(array);
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.deepCopy();
		}
		
		public void insert(int index, float value){
			if(index < 0 || index > array.length){
				System.out.println("Index out of bounds");
			}
			array[index] = value;
		}
		
		public float read(int index){
			if(index < 0 || index > array.length){
				System.out.println("Index out of bounds");
			}
			return array[index];
		}
		
	}
	
	public static class GoFloat64Array extends GoArray{

		private double[] array;
		
		public GoFloat64Array(int length) {
			super(null, null);
			this.length = length;
			array = new double[length];
		}
		
		@Override
		public String toString(){
			return Arrays.toString(array);
		}
		
		@Override
		public GoPrimitiveTypes getType(){
			return GoPrimitiveTypes.FLOAT64;
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.deepCopy();
		}
		
		public void insert(int index, double value){
			if(index < 0 || index > array.length){
				System.out.println("Index out of bounds");
			}
			array[index] = value;
		}
		
		public double read(int index){
			if(index < 0 || index > array.length){
				System.out.println("Index out of bounds");
			}
			return array[index];
		}
		
	}
	
	public static class GoStringArray extends GoArray{

		private String[] array;
		
		public GoStringArray(int length) {
			super(null, null);
			this.length = length;
			array = new String[length];
			Arrays.fill(array, "");
		}
		
		@Override
		public String toString(){
			return Arrays.toString(array);
		}
		
		@Override
		public GoPrimitiveTypes getType(){
			return GoPrimitiveTypes.STRING;
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.deepCopy();
		}
		
		public void insert(int index, String value){
			if(index < 0 || index > array.length){
				System.out.println("Index out of bounds");
			}
			array[index] = value;
		}
		
		public String read(int index){
			if(index < 0 || index > array.length){
				System.out.println("Index out of bounds");
			}
			return array[index];
		}
		
	}
	
	public static class GoObjectArray extends GoArray{

		private Object[] array;
		
		public GoObjectArray(int length) {
			super(null, null);
			this.length = length;
			array = new Object[length];
		}
		
		@Override
		public GoPrimitiveTypes getType(){
			return GoPrimitiveTypes.OBJECT;
		}
		
		@Override
		public String toString(){
			return Arrays.toString(array);
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.deepCopy();
		}
		
		public void insert(int index, Object value){
			if(index < 0 || index > array.length){
				System.out.println("Index out of bounds");
			}
			array[index] = value;
		}
		
		public Object read(int index){
			if(index < 0 || index > array.length){
				System.out.println("Index out of bounds");
			}
			return array[index];
		}
		
	}

}
