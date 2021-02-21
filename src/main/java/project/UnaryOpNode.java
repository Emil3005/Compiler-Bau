package project;

public class UnaryOpNode extends SyntaxNode implements IVisitable {
    public String operator;
    public IVisitable subNode;

    public UnaryOpNode(String operator, IVisitable subNode) {
        this.operator = operator;
        this.subNode = subNode;
    }

    @Override
    public void accept(IVisitor IVisitor) {
        IVisitor.visit(this);
    }
}
