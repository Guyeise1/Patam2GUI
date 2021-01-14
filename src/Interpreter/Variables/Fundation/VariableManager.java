package Interpreter.Variables.Fundation;

import java.util.Collection;
import java.util.concurrent.locks.Lock;

public interface VariableManager {
    void createVariable(String variableName);

    void setValue(String var, Double value);

    boolean containsVariable(String variableName);

    Double getValue(String variableName);

    void bind(String subscriber, String publisher);

    Collection<String> getAll();


    void clear();

    String placement(String s);

    Lock readLock();

    Lock writeLock();
}
