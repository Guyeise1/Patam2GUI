package Interpreter.Commands.Fundation;

import Interpreter.Commands.util.Variable;
import simulator.NetworkCommands;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public final class VariablesFactory implements Variables {



    private static VariablesFactory instance;

    public static VariablesFactory getInstance() {
        if (instance == null) {
            instance = new VariablesFactory();
        }
        return instance;
    }

    private Map<String, Variable> variables;
    private List<String> variableToAutoUpdate = new CopyOnWriteArrayList<>();

    /**
     * Variables class is a singleton, do not allow access
     */
    private VariablesFactory() {
        new Thread(this::autoUpdate).start();
        clean();
    }

    @Override
    public synchronized void createVariable(String variableName) {
        ensureEmptyVariable(variableName);
        Variable reference = new Variable(variableName, null);
        variables.put(variableName, reference);
    }

    @Override
    public synchronized void assignValue(String var, double value) {
        ensureExistingVariable(var);
        Variable reference = variables.get(var);
        reference.value = value;
        if(variableToAutoUpdate.contains(var)) {
            simulator.Parameters.setDoubleValue(var.replace("\"", ""), value);
        }
    }

    public synchronized void assignValueNoUpdate(String var, double value) {
        ensureExistingVariable(var);
        Variable reference = variables.get(var);
        reference.value = value;
    }

    @Override
    public synchronized Double getValue(String variable) {
        return getVariable(variable).value;
    }

    @Override
    public synchronized Variable getVariable(String variable) {
        ensureExistingVariable(variable);
        return variables.get(variable);
    }

    @Override
    public synchronized void bind(String origin, String reference) {
        if (!containsVariable(origin)) {
            createVariable(origin);
        }

        if (!containsVariable(reference)) {

            createVariable(reference);
        }
        Variable originVariable = variables.get(origin);
        variables.put(reference, originVariable);
        this.variableToAutoUpdate.add(originVariable.name);
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void autoUpdate() {
        while(true) {
            try {
                this.variableToAutoUpdate.forEach(this::updateVaraibleFromSimulator);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param variableName - a path in the simulator
     */
    private void updateVaraibleFromSimulator(String variableName) {
        this.assignValueNoUpdate(variableName, simulator.Parameters.getDoubleValue(variableName.replace("\"", "")).orElse(-1.0));
    }

    @Override
    public synchronized Collection<Variable> allVariables() {
        return variables.values();
    }

    @Override
    public Collection<String> allVariableNames() {
        return variables.keySet();
    }


    public synchronized boolean containsVariable(String var) {
        return variables.containsKey(var);
    }

    @Override
    public synchronized void clean() {
        this.variables = new HashMap<>();
    }

    private synchronized void ensureExistingVariable(String var) {
        if (!containsVariable(var)) {
            throw new RuntimeException("no variable " + var + " found");
        }
    }

    private synchronized void ensureEmptyVariable(String var) {
        if (containsVariable(var)) {
            throw new RuntimeException("variable " + var + " already exists");
        }
    }


}
