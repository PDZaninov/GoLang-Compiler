	package com.oracle.app.nodes.types;

import java.util.Arrays;

import com.oracle.app.GoException;
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
	public GoNonPrimitiveType doCompositeLit(VirtualFrame frame, Object[] results) {
		return null;
	}
	
	@Override
	public Object read(Object index){
		return null;
	}
	
	@Override
	public void insert(Object index, Object value) {
	}
	
	public boolean compare(GoArray other){
		throw new GoException("Undefind GoArray");
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame){
		length = lengthnode.executeInteger(frame);
		Object kind = typeexpr.executeGeneric(frame);
		GoArrayLikeTypes result = null;
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
		if(length == 0){
			result = GoSlice.createGoSlice(result, 0, 0, 0);
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
		public GoNonPrimitiveType doCompositeLit(VirtualFrame frame, Object[] results) {
			for(int i = 0; i < results.length; i++){
				array[i] = (int) results[i];
			}
			return this;
		}
		
		@Override
		public String toString(){
			return Arrays.toString(array);
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.deepCopy();
		}
		
		@Override
		public Object read(Object index){
			return array[(int) index];
		}
		
		@Override
		public void insert(Object index, Object value) {
			array[(int) index] = (int) value;
		}
		
		public void insert(int index, int value){
			if(index < 0 || index > array.length){
				throw new GoException("Index out of range");
			}
			array[index] = value;
		}
		
		public int read(int index){
			if(index < 0 || index > array.length){
				throw new GoException("Index out of range");
			}
			return array[index];
		}

		@Override
		public boolean compare(GoArray other) {
			return other instanceof GoIntArray;
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
		public GoNonPrimitiveType doCompositeLit(VirtualFrame frame, Object[] results) {
			for(int i = 0; i < results.length; i++){
				array[i] = (float) results[i];
			}
			return this;
		}
		
		@Override
		public String toString(){
			return Arrays.toString(array);
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.deepCopy();
		}
		
		@Override
		public Object read(Object index){
			return array[(int) index];
		}
		
		@Override
		public void insert(Object index, Object value) {
			array[(int) index] = (float) value;
		}
		
		public void insert(int index, float value){
			if(index < 0 || index > array.length){
				throw new GoException("Index out of range");
			}
			array[index] = value;
		}
		
		public float read(int index){
			if(index < 0 || index > array.length){
				throw new GoException("Index out of range");
			}
			return array[index];
		}
		
		@Override
		public boolean compare(GoArray other) {
			return other instanceof GoFloat32Array;
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
		public GoNonPrimitiveType doCompositeLit(VirtualFrame frame, Object[] results) {
			for(int i = 0; i < results.length; i++){
				array[i] = (double) results[i];
			}
			return this;
		}
		
		@Override
		public GoPrimitiveTypes getType(){
			return GoPrimitiveTypes.FLOAT64;
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.deepCopy();
		}
		
		@Override
		public Object read(Object index){
			return array[(int) index];
		}
		
		@Override
		public void insert(Object index, Object value) {
			array[(int) index] = (double) value;
		}
		
		public void insert(int index, double value){
			if(index < 0 || index > array.length){
				throw new GoException("Index out of range");
			}
			array[index] = value;
		}
		
		public double read(int index){
			if(index < 0 || index > array.length){
				throw new GoException("Index out of range");
			}
			return array[index];
		}
		
		@Override
		public boolean compare(GoArray other) {
			return other instanceof GoFloat64Array;
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
		public GoNonPrimitiveType doCompositeLit(VirtualFrame frame, Object[] results) {
			for(int i = 0; i < results.length; i++){
				array[i] = (String) results[i];
			}
			return this;
		}
		
		@Override
		public GoPrimitiveTypes getType(){
			return GoPrimitiveTypes.STRING;
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.deepCopy();
		}
		
		@Override
		public Object read(Object index){
			return array[(int) index];
		}
		
		@Override
		public void insert(Object index, Object value) {
			array[(int) index] = (String) value;
		}
		
		public void insert(int index, String value){
			if(index < 0 || index > array.length){
				throw new GoException("Index out of range");
			}
			array[index] = value;
		}
		
		public String read(int index){
			if(index < 0 || index > array.length){
				throw new GoException("Index out of range");
			}
			return array[index];
		}
		
		@Override
		public boolean compare(GoArray other) {
			return other instanceof GoStringArray;
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
		public GoNonPrimitiveType doCompositeLit(VirtualFrame frame, Object[] results) {
			for(int i = 0; i < results.length; i++){
				array[i] = results[i];
			}
			return this;
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.deepCopy();
		}
		
		@Override
		public Object read(Object index){
			return array[(int) index];
		}
		
		@Override
		public void insert(Object index, Object value) {
			array[(int) index] = value;
		}
		
		public void insert(int index, Object value){
			if(index < 0 || index > array.length){
				throw new GoException("Index out of range");
			}
			array[index] = value;
		}
		
		public Object read(int index){
			if(index < 0 || index > array.length){
				throw new GoException("Index out of range");
			}
			return array[index];
		}
		
		@Override
		public boolean compare(GoArray other) {
			return other instanceof GoObjectArray;
		}
		
	}

}
