package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Objects that are not listed as a primitive type under {@link GoPrimitiveTypes}
 * fall under a non primitive type. Non primitive types can be initialized via Composite Lits
 * @author Trevor
 *
 */
public abstract class GoNonPrimitiveType extends GoExpressionNode {
	public abstract GoNonPrimitiveType doCompositeLit(VirtualFrame frame, Object[] results);
}
