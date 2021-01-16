package Interpreter.Commands.Exceptions;

import javax.script.ScriptException;

public class ParseException extends InvalidArgumentsException {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(){
        super();
    }

    public ParseException(Exception e) {
        super(e);
    }
}
