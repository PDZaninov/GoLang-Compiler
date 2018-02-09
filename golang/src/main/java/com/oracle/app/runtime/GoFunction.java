package com.oracle.app.runtime;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.utilities.CyclicAssumption;
import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoUndefinedFunctionRootNode;

/**
 * Represents a SL function. On the Truffle level, a callable element is represented by a
 * {@link RootCallTarget call target}. This class encapsulates a call target, and adds version
 * support: functions in SL can be redefined, i.e. changed at run time. When a function is
 * redefined, the call target managed by this function object is changed (and {@link #callTarget} is
 * therefore not a final field).
 * <p>
 * Function redefinition is expected to be rare, therefore optimized call nodes want to speculate
 * that the call target is stable. This is possible with the help of a Truffle {@link Assumption}: a
 * call node can keep the call target returned by {@link #getCallTarget()} cached until the
 * assumption returned by {@link #getCallTargetStable()} is valid.
 * <p>
 * The {@link #callTarget} can be {@code null}. To ensure that only one {@link SLFunction} instance
 * per name exists, the {@link SLFunctionRegistry} creates an instance also when performing name
 * lookup. A function that has been looked up, i.e., used, but not defined, has a call target that
 * encapsulates a {@link SLUndefinedFunctionRootNode}.
 */
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


    /**
     * This method is, e.g., called when using a function literal in a string concatenation. So
     * changing it has an effect on SL programs.
     */
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