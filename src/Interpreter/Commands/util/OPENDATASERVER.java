package Interpreter.Commands.util;

import Interpreter.CalcExpresion;
import Interpreter.Commands.Exceptions.*;
import Interpreter.Commands.Fundation.BinaryCommand;
import Interpreter.Network.Client;
import Interpreter.Network.Server;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

public class OPENDATASERVER extends BinaryCommand<Void> {

    public static Integer frequency;
    public static Integer port;

    public static AtomicBoolean shouldRun = new AtomicBoolean(false);

    @Override
    public Void execute() throws CommandNotFoundException, InstantiationException, InvocationTargetException, NoSuchMethodException, InvalidArgumentsException, IllegalAccessException, InterpreterException, InvalidConditionFormatException, NoCommandsLeftException, CalculateException {

        OPENDATASERVER.shouldRun.set(true);
        port = (int) CalcExpresion.calc(this.getArgs()[1]);
        frequency = (int) CalcExpresion.calc(this.getArgs()[2]);

        Server.getInstance().setPort(port);
        Client.getInstance().setFrequency(frequency);
        Server.getInstance().start();

        return null;
    }
}
