package project;

public class Parser {
    private int position;
    private final String eingabe;
//...
    public Parser(String eingabe)
    {
        this.eingabe = eingabe;
        this.position = 0;
    }
//...
    private void match(char symbol)
    {
        if ((eingabe == null) || ("".equals(eingabe)))
        {throw new RuntimeException("Syntax error !");
        }
        if (position >= eingabe.length())
        {
            throw new RuntimeException("End of input reached !");
        }
        if (eingabe.charAt(position) != symbol)
        {
            throw new RuntimeException("Syntax error !");
        }
        position++;
    }
//------------------------------------------------------------------
// 1. wird benoetigt bei der Regel Start -> '(' RegExp ')''#'
// 2. wird benoetigt bei der Regel Start -> '#'
// 3. wird sonst bei keiner anderen Regel benoetigt
//------------------------------------------------------------------
    private void assertEndOfInput()
    {
        if (position < eingabe.length())
        {
            throw new RuntimeException(" No end of input reached !");
        }
    }
}
