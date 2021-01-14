package Interpreter.Variables.Fundation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class VariableProvider implements VariableManager {

    private static VariableProvider instance;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<String, Double> variables = new HashMap<>();
    private final Map<String, String> binders = new HashMap<>();

    private VariableProvider() {
    }

    public static VariableProvider getInstance() {
        if (instance == null) {
            instance = new VariableProvider();
        }
        return instance;
    }

    @Override
    public void createVariable(String variableName) {
        lock.writeLock().lock();
        try {
            setValue(variableName, null);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void setValue(String variableName, Double value) {
        lock.writeLock().lock();
        try {
            variables.put(binders.getOrDefault(variableName, variableName), value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsVariable(String variableName) {
        lock.readLock().lock();
        try {
            return variables.containsKey(variableName) || binders.containsKey(variableName);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Double getValue(String variableName) {
        lock.readLock().lock();
        try {
            if (variables.containsKey(variableName)) {
                return variables.get(variableName);
            }
            if (binders.containsKey(variableName)) {
                return getValue(binders.get(variableName));
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void bind(String subscriber, String publisher) {
        lock.writeLock().lock();
        try {
            variables.remove(subscriber);
            binders.put(subscriber, publisher);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void unbind(String subscriber) {
        lock.writeLock().lock();
        try {
            binders.remove(subscriber);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Collection<String> getAll() {
        lock.readLock().lock();
        try {
            Collection<String> ret = new ArrayList<>();
            ret.addAll(variables.keySet());
            ret.addAll(binders.keySet());
            return ret;
        } finally {
            lock.readLock().unlock();
        }
    }


    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            variables.clear();
            binders.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String placement(String s) throws NullPointerException {
        lock.readLock().lock();
        try {
            for (String var : getAll()) {
                if (var == null) {
                    throw new NullPointerException("var " + var + " is null");
                }
                s = s.replace(var, String.valueOf(getValue(var)));
            }
            return s;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Lock readLock() {
        return lock.readLock();
    }

    @Override
    public Lock writeLock() {
        return lock.writeLock();
    }
}
