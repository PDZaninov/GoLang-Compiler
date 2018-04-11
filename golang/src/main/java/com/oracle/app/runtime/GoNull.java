package com.oracle.app.runtime;

import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;

public class GoNull implements TruffleObject{

	public static final GoNull SINGLETON = new GoNull();
	
	private GoNull() {
	}
	
	public String toString(){
		return "nil";
	}

	@Override
	public ForeignAccess getForeignAccess() {
		return GoNullMessageResolutionForeign.ACCESS;
	}
	

}
