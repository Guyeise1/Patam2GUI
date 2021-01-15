package Interpreter.Commands.util;

import Interpreter.Commands.Exceptions.*;
import Interpreter.Commands.Fundation.ConditionalCommand;

import java.lang.reflect.InvocationTargetException;

public class WHILE extends ConditionalCommand {
    @Override
    public Void execute() throws CommandNotFoundException, InstantiationException, InvocationTargetException, NoSuchMethodException, InvalidArgumentsException, IllegalAccessException, InterpreterException, InvalidConditionFormatException, NoCommandsLeftException, CalculateException {
        while(this.getCondition().calculate()) {
            this.getCodeBlock().execute();
        }

        return null;
    }
}
