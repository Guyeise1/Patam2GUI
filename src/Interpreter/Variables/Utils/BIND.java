package Interpreter.Variables.Utils;

import Interpreter.Commands.Exceptions.InvalidArgumentsException;
import Interpreter.Variables.Fundation.VariableCommand;

public class BIND extends VariableCommand {

    private String variablePublisherName;
    private String variableSubscriberName;

    public String getVariableSubscriberName() {
        return this.variableSubscriberName;
    }

    public String getVariablePublisherName() {
        return this.variablePublisherName;
    }


    @Override
    public Void execute() {
        manager.bind(this.variableSubscriberName, this.variablePublisherName);
        return null;
    }

    @Override
    protected void parseArgs() throws InvalidArgumentsException {
        if (this.getArgs().length != 2) {
            throw new InvalidArgumentsException(this.getClass().getName() + " gets 2 arguments");
        }
        this.variableSubscriberName = getArgs()[0];
        this.variablePublisherName = getArgs()[1];
    }
}
