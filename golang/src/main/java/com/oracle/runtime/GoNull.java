package com.oracle.runtime;

import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;

public final class GoNull implements TruffleObject{

	public static final GoNull SINGLETON = new GoNull();
	
	private GoNull(){
	}
	
	/*
	 *
	 * Might not be the right thing to be returning here maybe??
	 */
	@Override
	public String toString(){
		return "null";
	}
	
	@Override
	public ForeignAccess getForeignAccess() {
		// Apparently a different access thingy should be here to allow other languages to access this?
		return null;
	}

}
