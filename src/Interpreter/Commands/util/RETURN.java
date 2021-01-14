package Interpreter.Commands.util;

import Interpreter.CalcExpresion;
import Interpreter.Commands.Exceptions.ParseException;
import Interpreter.Commands.Fundation.UnaryCommand;
import Interpreter.Variables.Fundation.VariableProvider;

public class RETURN extends UnaryCommand<Integer> {

    public static final String RETURN_COMMAND_NAME = "return";
    private Integer returnStatus;

    public RETURN() {
        super(RETURN_COMMAND_NAME);
    }

    @Override
    public Integer execute() throws ParseException {
        String exp = "";
        for (int i = 1; i < getArgs().length; i++) {
            exp = exp.concat(getArgs()[i]);
        }
        String assigned = null;
        while (assigned == null) {
            try {
                assigned = VariableProvider.getInstance().placement(exp);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return (int) CalcExpresion.calc(assigned);
    }

    public Integer getReturnStatus() {
        return returnStatus;
    }
}

