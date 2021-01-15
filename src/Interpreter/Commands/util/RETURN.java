package Interpreter.Commands.util;

import Interpreter.CalcExpresion;
import Interpreter.Commands.Exceptions.InvalidArgumentsException;
import Interpreter.Commands.Fundation.UnaryCommand;
import test.MyInterpreter;

public class RETURN extends UnaryCommand<Integer> {

    private Integer returnStatus;

    public static final String RETURN_COMMAND_NAME = "return";

    public RETURN() {
        super(RETURN_COMMAND_NAME);
    }

    @Override
    public Integer execute() {
        Integer returnStatus = getReturnStatus();
        if (returnStatus == null) {
            throw new VerifyError("Impossible return command: no value set for return");
        }
        //System.exit(returnStatus);
        return returnStatus;
    }

    /**
     * Build a a return command out of string of arguments.
     * Assumption is args[0] = 'return' and args[1...length] = expression: 3 + 5 - 2 * 8 ....
     * @param args the arguments of command
     * @throws InvalidArgumentsException if arguments do not match above order.
     */
    @Override
    public void setArgs(String[] args) throws InvalidArgumentsException {
        MyInterpreter instance = MyInterpreter.getInstance();
        String[] parseVariblesArguemnts = instance.assignVariableValues(args);
        super.setArgs(parseVariblesArguemnts);
        String commandArgument = getCommandArgument();
        this.returnStatus = (int) CalcExpresion.calc(commandArgument);
    }

    public Integer getReturnStatus() {
        return returnStatus;
    }
}

