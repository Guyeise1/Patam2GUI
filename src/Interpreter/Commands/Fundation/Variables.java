package Interpreter.Commands.Fundation;

import Interpreter.Commands.util.Variable;

import java.util.Collection;

public interface Variables {

    void createVariable(String variableName);

    void assignValue(String var, double value);

    boolean containsVariable(String variableName);

    Double getValue(String variable);

    Variable getVariable(String variable);

    void bind(String existingVariable, String newVariable);

    Collection<Variable> allVariables();

    Collection<String> allVariableNames();

    void clean();

}
