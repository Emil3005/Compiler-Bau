import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import project.Parser;
import project.Visitable;

public class ParserTest {
    @Test
    public void InvalidSyntax_MissingParenthesis() {
        Parser parser = new Parser("(a#");
        assertThrows(RuntimeException.class, () -> parser.start(null));
    }

    @Test
    public void InvalidSyntax_MissingHash(){
        Parser parser = new Parser("(a*b*)");
        assertThrows(RuntimeException.class, () -> parser.start(null));
    }

}
