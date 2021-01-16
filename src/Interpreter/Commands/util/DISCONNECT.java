package Interpreter.Commands.util;

import Interpreter.Commands.Exceptions.*;
import Interpreter.Commands.Fundation.Command;
import Interpreter.Network.Client;
import Interpreter.Network.Server;

import java.lang.reflect.InvocationTargetException;

public class DISCONNECT extends Command<Void> {
    @Override
    public Void execute() throws CommandNotFoundException, InstantiationException, InvocationTargetException, NoSuchMethodException, InvalidArgumentsException, IllegalAccessException, InterpreterException, InvalidConditionFormatException, NoCommandsLeftException, CalculateException {
        Server.getInstance().stop();
        Client.getInstance().stop();
        return null;
    }
}
