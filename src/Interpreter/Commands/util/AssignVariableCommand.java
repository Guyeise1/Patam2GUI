package Interpreter.Commands.util;

import Interpreter.CalcExpresion;
import Interpreter.Commands.Exceptions.*;
import Interpreter.Commands.Fundation.Command;
import Interpreter.Commands.Fundation.Variables;
import test.MyInterpreter;

import java.lang.reflect.InvocationTargetException;

public final class AssignVariableCommand extends Command<Void> {

    public static final String ASSIGN_KEYWORD = "=";
    @Override
    public Void execute() {
        return null;
    }

    @Override
    public void setArgs(String... args) throws InvalidArgumentsException {
        String entireCommand = String.join(" ", args);
        if (!entireCommand.contains(ASSIGN_KEYWORD)) {
            throw new InvalidArgumentsException("Assign command must contain assignment.");
        }
        int indexOfAssign = entireCommand.indexOf(ASSIGN_KEYWORD);
        String leftWingAssignment = entireCommand.substring(0, indexOfAssign);
        String rightWingAssignment = entireCommand.substring(indexOfAssign + 1, entireCommand.length() );

        Variable variable = null;
        Variables variables = MyInterpreter.getInstance().getVariablesFactory();

        String createVariableName = CreateVariableCommand.CREATE_VARIABLE_NAME;
        if (leftWingAssignment.contains(createVariableName)) {
            try {
                variable = (Variable) Command.parse(leftWingAssignment).execute();
            } catch (CommandNotFoundException exception) {
                throw new InvalidArgumentsException("impossible to create a command");
            } catch (NoCommandsLeftException | CalculateException | NoSuchMethodException | InvalidConditionFormatException | InvocationTargetException | IllegalAccessException | InstantiationException | InterpreterException e) {
                e.printStackTrace();
            }
        } else {
            String variableName = leftWingAssignment.replace(" ", "");
            variable = variables.getVariable(variableName);
        }

        if (rightWingAssignment.contains("bind")) {
            String bindToVariable = rightWingAssignment
                    .replace("bind", "").replace(" ", "");
            variables.bind(bindToVariable, variable.name);
        } else {
            String expressionParseVariables = MyInterpreter.getInstance().assignVariableValues(rightWingAssignment);
            double calcExpression = CalcExpresion.calc(expressionParseVariables);
            variables.assignValue(variable.name, calcExpression);
        }
    }
}