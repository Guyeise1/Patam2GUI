package Interpreter.Commands.Fundation;

import Interpreter.Commands.Exceptions.InvalidArgumentsException;

public abstract class UnaryCommand<T> extends Command<T> {

    String commandArgument;

    public UnaryCommand() {
        super();
    }

    public UnaryCommand(String commandName) {
        super(commandName);
    }

    @Override
    public void setArgs(String... args) throws InvalidArgumentsException {
        String commandName = getName();
        if (args.length <= 1) {
            throw new InvalidArgumentsException("Command " + commandName +
                    " was not given enough parameters");
        } else if (!args[0].equals(commandName)) {
            throw new InvalidArgumentsException("Command " + commandName
                    + " did not start with command name");
        }
        StringBuilder argumentBuffer = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            argumentBuffer.append(args[i]);
        }
        setCommandArgument(argumentBuffer.toString());
        super.setArgs(args);

    }

    public String getCommandArgument() {
        return commandArgument;
    }

    protected void setCommandArgument(String commandArgument) {
        this.commandArgument = commandArgument;
    }
}
