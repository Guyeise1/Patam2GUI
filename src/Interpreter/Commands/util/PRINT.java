package Interpreter.Commands.util;

import Interpreter.Commands.Exceptions.*;
import Interpreter.Commands.Fundation.UnaryCommand;
import test.MyInterpreter;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PRINT extends UnaryCommand<Void> {
    @Override
    public Void execute() throws CommandNotFoundException, InstantiationException, InvocationTargetException, NoSuchMethodException, InvalidArgumentsException, IllegalAccessException, InterpreterException, InvalidConditionFormatException, NoCommandsLeftException, CalculateException {
        System.out.println(Arrays.stream(this.getArgs()).map(x -> MyInterpreter.getInstance().assignVariableValues(x)).collect(Collectors.joining(" ")));
        return null;
    }


}
