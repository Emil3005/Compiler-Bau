
import org.junit.jupiter.api.Test;
import project.*;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class FirstVisTest {
    @Test
    public void firstVisTreeEquals(){
        FirstVisitor firstVisitor=new FirstVisitor();

        IVisitable left = new OperandNode("a");
        ((SyntaxNode)left).nullable=false;
        ((SyntaxNode)left).firstpos.add(1);
        ((SyntaxNode)left).lastpos.add(1);
        ((OperandNode)left).position=1;
        IVisitable right = new OperandNode("b");
        ((SyntaxNode)right).nullable=false;
        ((SyntaxNode)right).firstpos.add(2);
        ((SyntaxNode)right).lastpos.add(2);
        ((OperandNode)right).position=2;
        right = new OperandNode("a");
        ((SyntaxNode)right).nullable=false;
        ((SyntaxNode)right).firstpos.add(3);
        ((SyntaxNode)right).lastpos.add(3);
        ((OperandNode)right).position=3;

        left = new BinOpNode("|", left, right);
        ((SyntaxNode)left).nullable=false;
        ((SyntaxNode)left).firstpos.add(1);
        ((SyntaxNode)left).firstpos.add(2);
        ((SyntaxNode)left).lastpos.add(1);
        ((SyntaxNode)left).lastpos.add(2);
        left = new BinOpNode("°", left, right);
        ((SyntaxNode)left).nullable=false;
        ((SyntaxNode)left).firstpos.add(1);
        ((SyntaxNode)left).firstpos.add(2);
        ((SyntaxNode)left).firstpos.add(3);
        ((SyntaxNode)left).lastpos.add(3);

        left = new UnaryOpNode("*", left);
        ((SyntaxNode)left).nullable=true;
        ((SyntaxNode)left).firstpos.add(1);
        ((SyntaxNode)left).firstpos.add(2);
        ((SyntaxNode)left).lastpos.add(1);
        ((SyntaxNode)left).lastpos.add(2);





        right = new OperandNode("b");
        ((SyntaxNode)right).nullable=false;
        ((SyntaxNode)right).firstpos.add(4);
        ((SyntaxNode)right).lastpos.add(4);
        ((OperandNode)right).position=4;

        left = new BinOpNode("°", left, right);
        ((SyntaxNode)left).nullable=false;
        ((SyntaxNode)left).firstpos.add(1);
        ((SyntaxNode)left).firstpos.add(2);
        ((SyntaxNode)left).firstpos.add(3);
        ((SyntaxNode)left).lastpos.add(4);


        right = new OperandNode("b");
        ((SyntaxNode)right).nullable=false;
        ((SyntaxNode)right).firstpos.add(5);
        ((SyntaxNode)right).lastpos.add(5);
        ((OperandNode)right).position=5;

        left = new BinOpNode("°", left, right);
        ((SyntaxNode)left).nullable=false;
        ((SyntaxNode)left).firstpos.add(1);
        ((SyntaxNode)left).firstpos.add(2);
        ((SyntaxNode)left).firstpos.add(3);
        ((SyntaxNode)left).lastpos.add(5);


        right = new OperandNode("#");
        ((SyntaxNode)right).nullable=false;
        ((SyntaxNode)right).firstpos.add(6);
        ((SyntaxNode)right).lastpos.add(6);
        ((OperandNode)right).position=6;

        IVisitable refTree = new BinOpNode("°", left, right);
        ((SyntaxNode)refTree).nullable=false;
        ((SyntaxNode)refTree).firstpos.add(1);
        ((SyntaxNode)refTree).firstpos.add(2);
        ((SyntaxNode)refTree).firstpos.add(3);
        ((SyntaxNode)refTree).lastpos.add(6);


        right = new OperandNode("b");
        ((OperandNode)right).position=2;

        left = new OperandNode("a");
        ((OperandNode)left).position=1;

        left = new BinOpNode("|", left, right);

        left = new UnaryOpNode("*", left);

        right = new OperandNode("a");
        ((OperandNode)right).position=3;

        left = new BinOpNode("°", left, right);

        right = new OperandNode("b");
        ((OperandNode)right).position=4;

        left = new BinOpNode("°", left, right);

        right = new OperandNode("b");
        ((OperandNode)right).position=5;

        left = new BinOpNode("°", left, right);

        right = new OperandNode("#");
        ((OperandNode)right).position=6;

        IVisitable changeableTree = new BinOpNode("°", left, right);

        DepthFirstIterator.traverse(changeableTree,firstVisitor);


        assertTrue(FirstVisitorEquals(refTree,changeableTree));

    }
    public static boolean FirstVisitorEquals(IVisitable expected, IVisitable visited) {
        if (expected == null && visited == null) return true;
        if (expected == null || visited == null) return false;
        if (expected.getClass() != visited.getClass()) return false;
        if (expected.getClass() == BinOpNode.class)
        {
            BinOpNode op1 = (BinOpNode) expected;
            BinOpNode op2 = (BinOpNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos) &&
                    FirstVisitorEquals(op1.left, op2.left) && FirstVisitorEquals(op1.right, op2.right);
        }
        if (expected.getClass() == UnaryOpNode.class) {
            UnaryOpNode op1 = (UnaryOpNode) expected;
            UnaryOpNode op2 = (UnaryOpNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos) &&
                    FirstVisitorEquals(op1.subNode, op2.subNode);
        }
        if (expected.getClass() == OperandNode.class) {
            OperandNode op1 = (OperandNode) expected;
            OperandNode op2 = (OperandNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos);
        }
        throw new IllegalStateException(
                "invalid Arguments" +
                        expected.getClass().getSimpleName());


    }
    
}
