package Interpreter.Commands.util;

import Interpreter.Commands.Exceptions.*;
import Interpreter.Commands.Fundation.UnaryCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

public class SLEEP extends UnaryCommand<Void> {
    private int mili;

    @Override
    public Void execute() throws CommandNotFoundException, InstantiationException, InvocationTargetException, NoSuchMethodException, InvalidArgumentsException, IllegalAccessException, InterpreterException, InvalidConditionFormatException, NoCommandsLeftException, CalculateException {
        try {
            TimeUnit.MILLISECONDS.sleep(mili);
        } catch (InterruptedException ignored) {
        }

        return null;
    }

    @Override
    public void setArgs(String... args) throws InvalidArgumentsException {
        this.mili = Integer.parseInt(args[1]);
    }
}
