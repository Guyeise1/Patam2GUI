package Interpreter.Commands.Exceptions;

public class ParseException extends InvalidArgumentsException {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(){
        super();
    }

}
