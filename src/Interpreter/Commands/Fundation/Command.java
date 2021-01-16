package Interpreter.Commands.Fundation;

import Interpreter.Commands.Exceptions.*;
import Interpreter.Commands.util.VAR;

import java.lang.reflect.InvocationTargetException;

public abstract class Command<T> {

    private String commandName;

    public Command() {
        super();
    }

    public Command(String commandName) {
        setCommandName(commandName);
    }

    private String[] args;

    public abstract  T execute() throws CommandNotFoundException, InstantiationException, InvocationTargetException, NoSuchMethodException, InvalidArgumentsException, IllegalAccessException, InterpreterException, InvalidConditionFormatException, NoCommandsLeftException, CalculateException;
    public String getName() {
        return this.commandName == null ? this.getClass().getSimpleName().toLowerCase() : this.commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public static Command<?> parse(String s) throws InvalidArgumentsException, CommandNotFoundException {
        String[] args = s.split(" ");
        try {
            final Command<?> parsedCommand;
            if (s.contains("=")){
                parsedCommand = new VAR();
            } else {
                parsedCommand = CommandTranslator.getInstance().translate(args[0]).getDeclaredConstructor().newInstance();
            }
            parsedCommand.setArgs(args);
            return parsedCommand;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        throw new RuntimeException();
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
