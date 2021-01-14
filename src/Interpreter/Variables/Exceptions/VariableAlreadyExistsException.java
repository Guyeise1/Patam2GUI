package Interpreter.Variables.Exceptions;

public class VariableAlreadyExistsException extends VariableException {
    public VariableAlreadyExistsException() {
        super();
    }

    public VariableAlreadyExistsException(String s) {
        super(s);
    }
}
