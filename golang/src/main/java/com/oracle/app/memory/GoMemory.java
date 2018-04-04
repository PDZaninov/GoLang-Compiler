package com.oracle.app.memory;

import java.lang.reflect.Field;

import com.oracle.truffle.api.CompilerAsserts;

import sun.misc.Unsafe;

/**
 * Represents the Java memory. This is not the class to use when accessing Go runtime memory.
 * Go runtime addresses are represented using {@link GoAddress}. This class should only be called
 * by {@link GoAddress}.
 * This class only handles inserting and retrieving values from the java heap.
 * 
 * Currently only supports int pointers.
 * Missing: String, Char, Float, Imag.
 * 
 * Used the Truffle SuLong implementation as reference
 * https://github.com/graalvm/sulong/blob/master/projects/com.oracle.truffle.llvm.runtime/src/com/oracle/truffle/llvm/runtime/memory/LLVMMemory.java
 * @author Trevor
 *
 */
@SuppressWarnings("restriction")
public class GoMemory {

	private static final Unsafe unsafe = getUnsafe();
	
	private static Unsafe getUnsafe(){
		CompilerAsserts.neverPartOfCompilation();
		try{
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			return (Unsafe) theUnsafe.get(null);
		} catch(Exception e){
			throw new AssertionError();
		}
	}
	/*
	private static final GoMemory INSTANCE = new GoMemory();
	
	private GoMemory(){
	}
	*/
	public int getInt(long ptr){
		assert ptr != 0;
		return unsafe.getInt(ptr);
	}
	
	public void putInt(long ptr, int value){
		assert ptr != 0;
		unsafe.putInt(ptr, value);
	}
	
}
