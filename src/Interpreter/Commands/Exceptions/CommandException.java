package Interpreter.Commands.Exceptions;

public class CommandException extends Exception {

    public CommandException(String s) {
        super(s);
    }

    public CommandException() {

    }

    public CommandException(Exception e) {
        super(e);
    }
}
