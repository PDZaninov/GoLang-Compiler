

@NodeInfo(shortName = "func")
public final class GoFunctionLiteralNode extends GoExpressionNode {

    private final functionName;

    private final contextReference<GoContext> reference;

    public GoFunctionLiteralNode(GoLanguge languge, String functionName) {
        this.functionName = functionName;
        this.reference = language.getContextReference();
    }

    @Override
    public Object executeGeneric(VirtualFrame frame){
        return reference.get().getFunctionRegistery().lookup(functionName, true);
    }
}