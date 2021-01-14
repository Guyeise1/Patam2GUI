package Interpreter.Commands.util;

import Interpreter.Commands.Exceptions.*;
import Interpreter.Commands.Fundation.BinaryCommand;
import Interpreter.Network.Client;

import java.lang.reflect.InvocationTargetException;

public class CONNECT extends BinaryCommand<Void> {
    @Override
    public Void execute() throws CommandNotFoundException, InstantiationException, InvocationTargetException, NoSuchMethodException, InvalidArgumentsException, IllegalAccessException, InterpreterException, InvalidConditionFormatException, NoCommandsLeftException, CalculateException {
        Client.getInstance().setHostname(getArgs()[1]);
        Client.getInstance().setPort(Integer.parseInt(getArgs()[2]));
        Client.getInstance().start();
        return null;
    }
}
