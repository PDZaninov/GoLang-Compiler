package com.oracle.app.runtime;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.utilities.CyclicAssumption;
import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoUndefinedFunctionRootNode;


public final class GoFunction implements TruffleObject {

    /** The name of the function. */
    private final String name;

    /** The current implementation of this function. */
    private RootCallTarget callTarget;


    protected GoFunction(GoLanguage language, String name) {
        this.name = name;
        this.callTarget = Truffle.getRuntime().createCallTarget(new GoUndefinedFunctionRootNode(language, name));
    }

    public String getName() {
        return name;
    }

    protected void setCallTarget(RootCallTarget callTarget) {
        this.callTarget = callTarget;

    }

    public RootCallTarget getCallTarget() {
        return callTarget;
    }

    @Override
    public String toString() {
        return name;
    }

	@Override
	public ForeignAccess getForeignAccess() {
		// TODO Auto-generated method stub
		return null;
	}

}