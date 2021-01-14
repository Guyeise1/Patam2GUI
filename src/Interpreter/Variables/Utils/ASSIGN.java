package Interpreter.Variables.Utils;

import Interpreter.CalcExpresion;
import Interpreter.Commands.Exceptions.ParseException;
import Interpreter.Variables.Fundation.VariableCommand;

public class ASSIGN extends VariableCommand {
    private String variableValueExpression;
    private String variableName;

    @Override
    public Void execute() throws ParseException {
        String exp = manager.placement(variableValueExpression);
        Double variableValue = CalcExpresion.calc(exp);
        manager.setValue(variableName, variableValue);
        return null;
    }

    public String getVariableName() {
        return this.variableName;
    }

    @Override
    protected void parseArgs() {
        this.variableName = this.getArgs()[0];
        this.variableValueExpression = "";
        for (int i = 1; i < this.getArgs().length; i++) {
            variableValueExpression = variableValueExpression.concat(this.getArgs()[i]);
        }
    }
}
