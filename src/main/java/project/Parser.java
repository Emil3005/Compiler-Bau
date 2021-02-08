package project;

public class Parser {
    private int position;
    private final String eingabe;

    public Parser(String eingabe) {
        this.eingabe = eingabe;
        this.position = 0;
    }

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

    private void assertEndOfInput() {
        if (position < eingabe.length()) {
            throw new RuntimeException(" No end of input reached !");
        }
    }

    //TODO: Könnt mal nachschauen, ob das für euch auch Sinn macht (steht nahezu komplett auf S.20)

    public Visitable start(Visitable parameter){
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
            Visitable re = (term(null));
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

    private Visitable term(Visitable parameter){
        char curChar = eingabe.charAt(position);
        if (Character.isLetter(curChar) || Character.isDigit(curChar) || curChar == '('){
            if (parameter != null){
                return term(new BinOpNode("°", parameter, factor(null)));
            }else return term(factor(null));
        }else if (curChar == '|' || curChar == ')'){
            return parameter;
        }else throw new RuntimeException("Syntax error!");
    }


    private Visitable factor(Visitable parameter){
        char curChar = eingabe.charAt(position);
        if (Character.isLetter(curChar) || Character.isDigit(curChar) || curChar == '('){
            Visitable elem = elem(null);
            return hop(elem);
        }else throw new RuntimeException("Syntax error!");
    }

    private Visitable hop(Visitable parameter){
        char curChar = eingabe.charAt(position);
        if (Character.isLetter(curChar) || Character.isDigit(curChar) || curChar == '(' || curChar == '|' || curChar ==')'){
            return parameter;
        }else if (curChar == '*' || curChar == '+' || curChar == '?'){
            match(curChar);
            String curString = Character.toString(curChar); // Cast curChar to String for Node
            return new UnaryOpNode(curString, parameter);
        }else throw new RuntimeException("Syntax error!");
    }

    private Visitable elem(Visitable parameter){
        char curChar = eingabe.charAt(position);
        if (Character.isLetter(curChar) || Character.isDigit(curChar)){
            return alphanum(null);
        }else if(curChar == '('){
            match('(');
            Visitable regexp = regexp(null);
            match(')');
            return regexp;
        }else throw new RuntimeException("Syntax error!");
    }

    private Visitable alphanum(Visitable parameter){
        char curChar = eingabe.charAt(position);
        if (Character.isLetter(curChar) || Character.isDigit(curChar)){
            match(curChar);
            String curString = Character.toString(curChar);
            return new OperandNode(curString);
        }else throw new RuntimeException("Syntax error!");
    }

}