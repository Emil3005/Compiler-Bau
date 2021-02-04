package project;

public class Parser {
    private int position;
    private final String eingabe;

    //...
    public Parser(String eingabe) {
        this.eingabe = eingabe;
        this.position = 0;
    }

    //...
    private void match(char symbol) {
        if ((eingabe == null) || ("".equals(eingabe))) {
            throw new RuntimeException("Syntax error !");
        }
        if (position >= eingabe.length()) {
            throw new RuntimeException("End of input reached !");
        }
        if (eingabe.charAt(position) != symbol) {
            throw new RuntimeException("Syntax error !");
        }
        position++;
    }


    //TODO: Könnt mal nachschauen, ob das für euch auch Sinn macht (steht nahezu komplett auf S.20)

    private Visitable start(Visitable parameter){
        if (eingabe.charAt(position) == '('){
            match('(');
            Visitable subTree = regexp(null);
            match(')');
            match('#');
            assertEndOfInput();
            return new BinOpNode("°", subTree, new OperandNode("#"));
        }else if (eingabe.charAt(position) == '#'){
            match('#');
            assertEndOfInput();
            return new OperandNode("#");
        }else throw new RuntimeException("Syntax error !");
    }

    private Visitable regexp(Visitable parameter){
        char curChar = eingabe.charAt(position);
        if (Character.isLetter(curChar) || Character.isDigit(curChar) || curChar == '(') {
            Visitable term = term(null);
            Visitable re = (term);
            return re;
        }else throw new RuntimeException("Syntax error !");
    }

    private Visitable re(Visitable parameter){
        if (eingabe.charAt(position) == '|'){
            Visitable term = term(null);
            Visitable root = new BinOpNode("|", parameter,term);
            return re(root);
        }else if (eingabe.charAt(position) == ')'){
            return parameter;
        }else throw new RuntimeException("Syntax error!");
    }




    //------------------------------------------------------------------
// 1. wird benoetigt bei der Regel Start -> '(' RegExp ')''#'
// 2. wird benoetigt bei der Regel Start -> '#'
// 3. wird sonst bei keiner anderen Regel benoetigt
//------------------------------------------------------------------
    private void assertEndOfInput() {
        if (position < eingabe.length()) {
            throw new RuntimeException(" No end of input reached !");
        }
    }
}