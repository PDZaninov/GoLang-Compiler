 package com.oracle.app.nodes.types;

import com.oracle.app.nodes.types.GoArray.GoFloat32Array;
import com.oracle.app.nodes.types.GoArray.GoFloat64Array;
import com.oracle.app.nodes.types.GoArray.GoIntArray;
import com.oracle.app.nodes.types.GoArray.GoObjectArray;
import com.oracle.app.nodes.types.GoArray.GoStringArray;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Slice class which is just a reference to {@link GoArray}. Can be dynamically sized
 * but it does not grow the array dynamically. Because slices are just references, the
 * slices can be done by converting the index values to the window size the slice references.
 * @author Trevor
 *
 */
public abstract class GoSlice extends GoArrayLikeTypes {

	GoPrimitiveTypes type;
	int cap;
	int len;
	int low;
	int high;
	
	public static GoSlice createGoSlice(GoArrayLikeTypes array, int low, int high, int cap){
		if(array instanceof GoSlice){
			array = ((GoSlice) array).getArray();
			
		}
		switch(array.getType()){
		case FLOAT32:
			return new GoFloat32Slice((GoFloat32Array) array,low,high,cap);
		case FLOAT64:
			return new GoFloat64Slice((GoFloat64Array) array,low,high,cap);
		case INT:
			return new GoIntSlice((GoIntArray) array,low,high,cap);
		case OBJECT:
			return new GoObjectSlice((GoObjectArray) array,low,high,cap);
		case STRING:
			return new GoStringSlice((GoStringArray) array,low,high,cap);
		case BOOL:
		}
		return null;
	}
	
	protected abstract GoArrayLikeTypes getArray();

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return this;
	}
	
	@Override
	public int lowerBound(){
		return low;
	}
	
	@Override
	public int len(){
		return len;
	}
	
	@Override
	public int cap(){
		return cap;
	}
	
	@Override
	public GoPrimitiveTypes getType(){
		return type;
	}
	
	public static class GoIntSlice extends GoSlice{
		
		GoIntArray array;
		
		public GoIntSlice(GoIntArray array,int low, int high, int cap){
			this.array = array;
			this.low = low;
			this.high = high - 1;
			this.cap = cap;
			len = high - low;
			this.type = array.getType();
		}
		
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder("[");
			for(int i = low; i <= high; i++){
				sb.append(array.read(i)+" ");
			}
			if(sb.length() > 1){
				sb.deleteCharAt(sb.length()-1);
			}
			sb.append("]");
			return sb.toString();
		}
		
		public int read(int index){
			int realindex = index + low;
			//Error out, index out of bounds
			if(realindex < low || realindex > high){
				System.out.println("Slice index out of bounds");
			}
			return array.read(realindex);
		}
		
		public void insert(int index, int value){
			int realindex = index + low;
			//Error out, index out of bounds
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
				return;
			}
			array.insert(realindex, value);
		}
		
		@Override
		public GoNonPrimitiveType doCompositeLit(VirtualFrame frame, Object[] results) {
			array = new GoIntArray(results.length);
			this.low = 0;
			this.high = results.length - 1;
			this.cap = results.length;
			this.type = array.getType();
			len = high - low;
			for(int i = 0; i < results.length; i++){
				array.insert(i,(int) results[i]);
			}
			return this;
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.copy();
		}

		@Override
		protected GoArrayLikeTypes getArray() {
			return array;
		}

		@Override
		public Object read(Object index) {
			int realindex = (int)index + low;
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
				return null;
			}
			return array.read(realindex);
		}

		@Override
		public void insert(Object index, Object value) {
			int realindex = (int)index + low;
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
			}
			array.insert(realindex, value);
		}
	}
	
	public static class GoFloat32Slice extends GoSlice{
		
		GoFloat32Array array;
		
		public GoFloat32Slice(GoFloat32Array array,int low, int high, int cap){
			this.array = array;
			this.low = low;
			this.high = high - 1;
			this.cap = cap;
			len = high - low;
			this.type = array.getType();
		}
		
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder("[");
			for(int i = low; i <= high; i++){
				sb.append(array.read(i)+" ");
			}
			if(sb.length() > 1){
				sb.deleteCharAt(sb.length()-1);
			}
			sb.append("]");
			return sb.toString();
		}
		
		public float read(int index){
			int realindex = index + low;
			//Error out, index out of bounds
			if(realindex < low || realindex > high){
				System.out.println("Slice index out of bounds");
			}
			return array.read(realindex);
		}
		
		public void insert(int index, float value){
			int realindex = index + low;
			//Error out, index out of bounds
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
				return;
			}
			array.insert(realindex, value);
		}
		
		@Override
		public GoNonPrimitiveType doCompositeLit(VirtualFrame frame, Object[] results) {
			array = new GoFloat32Array(results.length);
			this.low = 0;
			this.high = results.length - 1;
			this.cap = results.length;
			this.type = array.getType();
			len = high - low;
			for(int i = 0; i < results.length; i++){
				array.insert(i,(float) results[i]);
			}
			return this;
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.copy();
		}

		@Override
		protected GoArrayLikeTypes getArray() {
			return array;
		}

		@Override
		public Object read(Object index) {
			int realindex = (int)index + low;
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
				return null;
			}
			return array.read(realindex);
		}

		@Override
		public void insert(Object index, Object value) {
			int realindex = (int)index + low;
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
			}
			array.insert(realindex, value);
		}
	}

	public static class GoFloat64Slice extends GoSlice{
		
		GoFloat64Array array;
		
		public GoFloat64Slice(GoFloat64Array array,int low, int high, int cap){
			this.array = array;
			this.low = low;
			this.high = high - 1;
			this.cap = cap;
			len = high - low;
			this.type = array.getType();
		}
		
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder("[");
			for(int i = low; i <= high; i++){
				sb.append(array.read(i)+" ");
			}
			if(sb.length() > 1){
				sb.deleteCharAt(sb.length()-1);
			}
			sb.append("]");
			return sb.toString();
		}
		
		public double read(int index){
			int realindex = index + low;
			//Error out, index out of bounds
			if(realindex < low || realindex > high){
				System.out.println("Slice index out of bounds");
			}
			return array.read(realindex);
		}
		
		public void insert(int index, double value){
			int realindex = index + low;
			//Error out, index out of bounds
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
				return;
			}
			array.insert(realindex, value);
		}
		
		@Override
		public GoNonPrimitiveType doCompositeLit(VirtualFrame frame, Object[] results) {
			array = new GoFloat64Array(results.length);
			this.low = 0;
			this.high = results.length - 1;
			this.cap = results.length;
			this.type = array.getType();
			len = high - low;
			for(int i = 0; i < results.length; i++){
				array.insert(i,(double) results[i]);
			}
			return this;
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.copy();
		}

		@Override
		protected GoArrayLikeTypes getArray() {
			return array;
		}

		@Override
		public Object read(Object index) {
			int realindex = (int)index + low;
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
				return null;
			}
			return array.read(realindex);
		}

		@Override
		public void insert(Object index, Object value) {
			int realindex = (int)index + low;
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
			}
			array.insert(realindex, value);
		}
	}
	
	public static class GoStringSlice extends GoSlice{
		
		GoStringArray array;
		
		public GoStringSlice(GoStringArray array,int low, int high, int cap){
			this.array = array;
			this.low = low;
			this.high = high - 1;
			this.cap = cap;
			len = high - low;
			this.type = array.getType();
		}
		
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder("[");
			for(int i = low; i <= high; i++){
				sb.append(array.read(i)+" ");
			}
			if(sb.length() > 1){
				sb.deleteCharAt(sb.length()-1);
			}
			sb.append("]");
			return sb.toString();
		}
		
		public String read(int index){
			int realindex = index + low;
			//Error out, index out of bounds
			if(realindex < low || realindex > high){
				System.out.println("Slice index out of bounds");
			}
			return array.read(realindex);
		}
		
		public void insert(int index, String value){
			int realindex = index + low;
			//Error out, index out of bounds
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
				return;
			}
			array.insert(realindex, value);
		}
		
		@Override
		public GoNonPrimitiveType doCompositeLit(VirtualFrame frame, Object[] results) {
			array = new GoStringArray(results.length);
			this.low = 0;
			this.high = results.length - 1;
			this.cap = results.length;
			this.type = array.getType();
			len = high - low;
			for(int i = 0; i < results.length; i++){
				array.insert(i,(String) results[i]);
			}
			return this;
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.copy();
		}

		@Override
		protected GoArrayLikeTypes getArray() {
			return array;
		}

		@Override
		public Object read(Object index) {
			int realindex = (int)index + low;
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
				return null;
			}
			return array.read(realindex);
		}

		@Override
		public void insert(Object index, Object value) {
			int realindex = (int)index + low;
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
			}
			array.insert(realindex, value);
		}
	}
	
	public static class GoObjectSlice extends GoSlice{
		
		GoObjectArray array;
		
		public GoObjectSlice(GoObjectArray array,int low, int high, int cap){
			this.array = array;
			this.low = low;
			this.high = high - 1;
			this.cap = cap;
			len = high - low;
			this.type = array.getType();
		}
		
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder("[");
			for(int i = low; i <= high; i++){
				sb.append(array.read(i)+" ");
			}
			if(sb.length() > 1){
				sb.deleteCharAt(sb.length()-1);
			}
			sb.append("]");
			return sb.toString();
		}
		
		public Object read(int index){
			int realindex = index + low;
			//Error out, index out of bounds
			if(realindex < low || realindex > high){
				System.out.println("Slice index out of bounds");
			}
			return array.read(realindex);
		}
		
		public void insert(int index, Object value){
			int realindex = index + low;
			//Error out, index out of bounds
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
				return;
			}
			array.insert(realindex, value);
		}
		
		@Override
		public GoNonPrimitiveType doCompositeLit(VirtualFrame frame, Object[] results) {
			array = new GoObjectArray(results.length);
			this.low = 0;
			this.high = results.length - 1;
			this.cap = results.length;
			this.type = array.getType();
			len = high - low;
			for(int i = 0; i < results.length; i++){
				array.insert(i, results[i]);
			}
			return this;
		}
		
		@Override
		public Object executeGeneric(VirtualFrame frame){
			return this.copy();
		}

		@Override
		protected GoArrayLikeTypes getArray() {
			return array;
		}

		@Override
		public Object read(Object index) {
			int realindex = (int)index + low;
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
				return null;
			}
			return array.read(realindex);
		}

		@Override
		public void insert(Object index, Object value) {
			int realindex = (int)index + low;
			if(realindex < low || realindex > high || realindex > cap){
				System.out.println("Slice index out of bounds");
			}
			array.insert(realindex, value);
		}
	}
}
