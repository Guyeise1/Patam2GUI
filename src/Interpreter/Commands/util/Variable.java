package Interpreter.Commands.util;

// TODO: 25/11/2020 Implement this class
public class Variable {

    public String name;

    public Double value;

    public Variable(String name, Double value) {
        this.value = value;
        this.name = name;
    }

    public boolean hasValue() {
        return value != null;
    }
}
