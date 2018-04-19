public class GoStructTypeExprNode extends GoExpressionNode{
    GoExpressionNode[] fields;

    public GoStructTypeExprNode(GoExpressionNode[] fields){
        this.fields = fields;
    }

    public Object executeGeneric(VirtualFrame frame){
        GoStruct result = new GoStruct();
        for(GoFieldNode child : fields){
            GoIdentNode type = child.getType();
            GoExpressionNode name = child.getNames()[0]; // This is the name of the variale
            //type.execute = Oject value of the type field, type.name = the type name such as int
            FieldNode field = new FieldNode(type.execute(), type.getName());
            result.insertField(name.execute(),field);
        }
        return result;
    }
}