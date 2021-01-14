package Interpreter.Variables.Utils;

import Interpreter.Commands.Exceptions.InvalidArgumentsException;
import Interpreter.Variables.Fundation.VariableCommand;

public class CREATE extends VariableCommand {

    private String variableName;

    @Override
    public Void execute() {
        manager.createVariable(this.getVariableName());
        return null;
    }

    public String getVariableName() {
        return this.variableName;
    }

    @Override
    protected void parseArgs() throws InvalidArgumentsException {
        if (this.getArgs().length != 1) {
            throw new InvalidArgumentsException(this.getClass().getName() + " gets 1 argument");
        }
        this.variableName = getArgs()[0];
    }
}
