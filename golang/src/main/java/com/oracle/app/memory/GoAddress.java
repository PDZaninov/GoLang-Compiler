package com.oracle.app.memory;

import com.oracle.truffle.api.CompilerDirectives.ValueType;

/**
 * Represents the GoLang runtime memory. Pointer values are
 * assigned using this class and will use the {@link GoMemory} to insert
 * into Java memory.
 * This class is used for pointer addresses, not direct memory access.
 * 
 * Currently only support int pointers
 * Missing: String, Char, Float, Imag.
 * 
 * Used the Truffle SuLong implementation as reference
 * https://github.com/graalvm/sulong/blob/master/projects/com.oracle.truffle.llvm.runtime/src/com/oracle/truffle/llvm/runtime/LLVMAddress.java
 * 
 * @author Trevor
 *
 */
@ValueType
public class GoAddress {

	private final long ptr;
	
	private GoAddress(long ptr){
		this.ptr = ptr;
	}
	
	public static GoAddress nullPointer(){
		return new GoAddress(0);
	}
	
	public static GoAddress fromLong(long val){
		return new GoAddress(val);
	}
	
	public long getPointer(){
		return ptr;
	}
	
	public String toString(){
		return String.format("0x%x", getPointer());
	}
}
