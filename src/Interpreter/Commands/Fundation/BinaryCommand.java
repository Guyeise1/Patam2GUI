package Interpreter.Commands.Fundation;

import Interpreter.Commands.Exceptions.InvalidArgumentsException;

public abstract class BinaryCommand<T> extends Command<T> {

    public BinaryCommand() {
        super();
    }

    public BinaryCommand(String commandName) {
        super(commandName);
    }


    @Override
    public void setArgs(String... args) throws InvalidArgumentsException {
        String commandName = getName();
        if (args.length != 3) {
            throw new InvalidArgumentsException("Command " + commandName +
                    " was not given enough parameters");
        }
        super.setArgs(args);
    }
}
