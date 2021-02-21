package project;

public class DepthFirstIterator {
    public static void traverse(IVisitable root, IVisitor IVisitor) {
        if (root instanceof OperandNode) {
            root.accept(IVisitor);
            return;
        }
        if (root instanceof BinOpNode) {
            BinOpNode opNode = (BinOpNode) root;
            DepthFirstIterator.traverse(opNode.left, IVisitor);
            DepthFirstIterator.traverse(opNode.right, IVisitor);
            opNode.accept(IVisitor);
            return;
        }
        if (root instanceof UnaryOpNode) {
            UnaryOpNode opNode = (UnaryOpNode) root;
            DepthFirstIterator.traverse(opNode.subNode, IVisitor);
            opNode.accept(IVisitor);
            return;
        }
        throw new RuntimeException("Instance root has a bad type!");
    }
}
