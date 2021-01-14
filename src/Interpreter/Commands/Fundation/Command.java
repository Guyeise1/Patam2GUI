package Interpreter.Commands.Fundation;

import Interpreter.Commands.Exceptions.*;

import java.lang.reflect.InvocationTargetException;

public abstract class Command<T> {

    private String commandName;
    private String[] args;

    public Command() {
        super();
    }

    public Command(String commandName) {
        setCommandName(commandName);
    }

    public abstract T execute() throws CommandNotFoundException, InstantiationException, InvocationTargetException, NoSuchMethodException, InvalidArgumentsException, IllegalAccessException, InterpreterException, InvalidConditionFormatException, NoCommandsLeftException, CalculateException;

    public String getName() {
        return this.commandName == null ? this.getClass().getSimpleName().toLowerCase() : this.commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    /***
     * Assign the value of variables
     */
    public void parseVariablesIntoNumbers() {

    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String... args) throws InvalidArgumentsException {
        this.args = args;
    }
}
