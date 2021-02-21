import org.junit.jupiter.api.Test;
import project.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {

    private static boolean equals(IVisitable v1, IVisitable v2){
        if (v1 == v2)
            return true;
        if (v1 == null)
            return false;
        if (v2 == null)
            return false;
        if (v1.getClass() != v2.getClass())
            return false;
        if (v1.getClass() == OperandNode.class)
        {
            OperandNode op1 = (OperandNode) v1;
            OperandNode op2 = (OperandNode) v2;
            return op1.position == op2.position && op1.symbol.equals(op2.symbol);
        }
        if (v1.getClass() == UnaryOpNode.class)
        {
            UnaryOpNode op1 = (UnaryOpNode) v1;
            UnaryOpNode op2 = (UnaryOpNode) v2;
            return op1.operator.equals(op2.operator) && equals(op1.subNode,
                    op2.subNode);
        }
        if (v1.getClass() == BinOpNode.class)
        {
            BinOpNode op1 = (BinOpNode) v1;
            BinOpNode op2 = (BinOpNode) v2;
            return op1.operator.equals(op2.operator) &&
                    equals(op1.left, op2.left)        &&
                    equals(op1.right, op2.right);
        }
        throw new IllegalStateException("Ungueltiger Knotentyp");
    }

    @Test
    public void ValidSyntax_Concatenation(){
        Parser parser = new Parser("(bca)#");
        IVisitable GeneratedTree = parser.start(null);

        IVisitable left = new OperandNode("b");
        ((OperandNode) left).position = 1;
        OperandNode right = new OperandNode("c");
        right.position = 2;
        left = new BinOpNode("°", left, right);
        right = new OperandNode("a");
        right.position = 3;
        left = new BinOpNode("°", left, right);
        right = new OperandNode("#");
        right.position = 4;
        IVisitable HardCodedTree = new BinOpNode("°", left, right);


        assertTrue(equals(GeneratedTree, HardCodedTree));
    }

    @Test
    public void ValidSyntax_Alternative(){
        Parser parser = new Parser("(a|b)#");
        IVisitable GeneratedTree = parser.start(null);

        IVisitable left = new OperandNode("a");
        ((OperandNode) left).position = 1;
        OperandNode right = new OperandNode("b");
        right.position = 2;
        left = new BinOpNode("|", left, right);
        right = new OperandNode("#");
        right.position = 3;
        IVisitable HardCodedTree = new BinOpNode("°", left, right);

        assertTrue(equals(GeneratedTree, HardCodedTree));
    }

    @Test
    public void ValidSyntax_Kleen(){
        Parser parser = new Parser("(a*)#");
        IVisitable GeneratedTree = parser.start(null);

        OperandNode subNode = new OperandNode("a");
        subNode.position = 1;
        IVisitable left = new UnaryOpNode("*", subNode);
        OperandNode right = new OperandNode("#");
        right.position = 2;
        IVisitable HardCodedTree = new BinOpNode("°", left, right);

        assertTrue(equals(GeneratedTree, HardCodedTree));
    }

    @Test
    public void ValidSyntax_Positive(){
        Parser parser = new Parser("(a+)#");
        IVisitable GeneratedTree = parser.start(null);

        OperandNode subNode = new OperandNode("a");
        subNode.position = 1;
        IVisitable left = new UnaryOpNode("+", subNode);
        OperandNode right = new OperandNode("#");
        right.position = 2;
        IVisitable HardCodedTree = new BinOpNode("°", left, right);

        assertTrue(equals(GeneratedTree,HardCodedTree));


    }

    @Test
    public void ValidSyntax_Option(){
        Parser parser = new Parser("(a?)#");
        IVisitable GeneratedTree = parser.start(null);

        OperandNode subNode = new OperandNode("a");
        subNode.position = 1;
        IVisitable left = new UnaryOpNode("?", subNode);
        OperandNode right = new OperandNode("#");
        right.position = 2;
        IVisitable HardCodedTree = new BinOpNode("°", left, right);

        assertTrue(equals(GeneratedTree, HardCodedTree));

    }


    @Test
    public void ValidSyntax_Complex(){
        Parser parser = new Parser("((a|b)*bc)#");
        IVisitable GeneratedTree = parser.start(null);

        IVisitable left = new OperandNode("a");
        ((OperandNode) left).position = 1;
        OperandNode right = new OperandNode("b");
        right.position = 2;
        left = new BinOpNode("|", left, right);
        left = new UnaryOpNode("*", left);
        right = new OperandNode("b");
        right.position = 3;
        left = new BinOpNode("°", left, right);
        right = new OperandNode("c");
        right.position = 4;
        left = new BinOpNode("°", left, right);
        right = new OperandNode("#");
        right.position = 5;
        IVisitable refTree = new BinOpNode("°", left, right);


        assertTrue(equals(GeneratedTree, refTree));
    }



    @Test
    public void InvalidSyntax_MissingStartParenthesis() {
        Parser parser = new Parser("a)#");
        assertThrows(RuntimeException.class, () -> parser.start(null));
    }

    @Test
    public void InvalidSyntax_MissingEndParenthesis() {
        Parser parser = new Parser("(a#");
        assertThrows(RuntimeException.class, () -> parser.start(null));
    }

    @Test
    public void InvalidSyntax_WrongOperatorPosition() {
        Parser parser = new Parser("(*ab)#");
        assertThrows(RuntimeException.class, () -> parser.start(null));
    }


    @Test
    public void InvalidSyntax_UnknownOperator() {
        Parser parser = new Parser("(a-b)#");
        assertThrows(RuntimeException.class, () -> parser.start(null));
    }

    @Test
    public void InvalidSyntax_MissingHash(){
        Parser parser = new Parser("(a*b*)");
        assertThrows(RuntimeException.class, () -> parser.start(null));
    }

}
