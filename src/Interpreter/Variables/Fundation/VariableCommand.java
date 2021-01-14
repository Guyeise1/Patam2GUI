package Interpreter.Variables.Fundation;

import Interpreter.Commands.Exceptions.InvalidArgumentsException;
import Interpreter.Commands.Exceptions.ParseException;
import Interpreter.Commands.Fundation.Command;

public abstract class VariableCommand extends Command<Void> {

    protected VariableManager manager = VariableProvider.getInstance();

    @Override
    public abstract Void execute() throws ParseException;

    @Override
    public void setArgs(String... args) throws InvalidArgumentsException {
        super.setArgs(args);
        parseArgs();
    }

    protected abstract void parseArgs() throws InvalidArgumentsException;
}
