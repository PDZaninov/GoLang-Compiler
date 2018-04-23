package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.app.runtime.GoContext;
import com.oracle.truffle.api.TruffleLanguage.ContextReference;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Load the built in library when executed
 * Will currently not handle user defined file imports
 * @author Trevor
 *
 */
public class GoImportSpec extends GoExpressionNode {
	
	@Child private GoStringNode child;
	private final ContextReference<GoContext> reference;
	
	public GoImportSpec(GoStringNode child, GoLanguage language) {
		this.child = child;
		reference = language.getContextReference();
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		String name = (String) child.executeGeneric(frame);
		switch(name) {
        case "fmt":
            reference.get().installFmt();
            break;
        default:
            System.out.println("Package not yet done");
		}
		return null;
	}
}