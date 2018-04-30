package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;

enum GoPrimitiveTypes {
	INT,
	FLOAT32,
	FLOAT64,
	STRING,
	BOOL,
	OBJECT
}

public abstract class GoPrimitiveType extends GoExpressionNode {

	public abstract GoPrimitiveTypes getType();

}
