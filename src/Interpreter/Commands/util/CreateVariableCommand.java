package Interpreter.Commands.util;

import Interpreter.Commands.Exceptions.InvalidArgumentsException;
import Interpreter.Commands.Fundation.UnaryCommand;
import test.MyInterpreter;


public class CreateVariableCommand extends UnaryCommand<Variable> {

    public static final String CREATE_VARIABLE_NAME = "var";

    public CreateVariableCommand() {
        super(CREATE_VARIABLE_NAME);
    }

    @Override
    public void setArgs(String... args) throws InvalidArgumentsException {
        if (args.length == 2) {
            super.setArgs(args);
        } else if (args.length == 1) {
            if (MyInterpreter.getInstance().getVariablesFactory().containsVariable(args[0])) {
                setCommandArgument(args[0]);
            }
        } else {
            throw new InvalidArgumentsException("Impossible command: " + String.join(" ", args));
        }
    }

    @Override
    public Variable execute() {
        String variableName = getCommandArgument();
        MyInterpreter.getInstance().getVariablesFactory().createVariable(variableName);
        return MyInterpreter.getInstance().getVariablesFactory().getVariable(variableName);
    }

    public static boolean isLegalVariableName(String optionalVariableName) {
        if (!optionalVariableName.matches("^[a-zA-Z]+$")) {
            return false;
        }
        if (MyInterpreter.getInstance().getVariablesFactory().containsVariable(optionalVariableName)) {
            return false;
        }

        return true;
    }
}
