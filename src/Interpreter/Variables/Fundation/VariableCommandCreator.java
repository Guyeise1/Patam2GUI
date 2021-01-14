package Interpreter.Variables.Fundation;

import Interpreter.Commands.Exceptions.InvalidArgumentsException;
import Interpreter.Variables.Utils.ASSIGN;
import Interpreter.Variables.Utils.BIND;
import Interpreter.Variables.Utils.CREATE;
import com.sun.istack.internal.NotNull;

public class VariableCommandCreator {
    private static VariableCommandCreator instance = null;

    private VariableCommandCreator() {
    }

    public static VariableCommandCreator getInstance() {
        if (instance == null) {
            instance = new VariableCommandCreator();
        }
        return instance;
    }

    /**
     * This method crete a new instance of the command based on the given line
     *
     * @param line - the command text. only one line and it must be clean (no whitespaces or tabs at the beginning and not empty string)
     * @return - the VariableCommand after translate or null if translate failed.
     */
    public VariableCommand parse(@NotNull String line) {
        if (line.contains("\n")) {
            return null;
        }
        try {
            if (line.contains("bind")) {
                return createBindCommand(line);
            } else if (line.contains("=") && !line.contains("==") && !line.contains(">=") && !line.contains("<=") && !line.contains("!=")) {
                return createAssignCommand(line);
            } else if (firstWord(line).equals("var")) {
                return createCreateVariableCommand(line);
            }
        } catch (InvalidArgumentsException e) {
            return null;
        }
        return null;
    }

    private VariableCommand createCreateVariableCommand(String line) throws InvalidArgumentsException {
        line = removeFirstWord(line); // Line always starts with "var"
        line = clearWhiteSpaces(line);
        CREATE ret = new CREATE();
        ret.setArgs(line);
        return ret;
    }

    private VariableCommand createAssignCommand(String line) throws InvalidArgumentsException {
        if (firstWord(line).equals("var")) {
            line = removeFirstWord(line);
        }
        line = clearWhiteSpaces(line);
        ASSIGN ret = new ASSIGN();
        ret.setArgs(line.split("="));
        return ret;
    }

    private VariableCommand createBindCommand(String line) throws InvalidArgumentsException {
        if (firstWord(line).equals("var")) {
            line = removeFirstWord(line);
        }
        line = line.replace("bind", "");
        line = clearWhiteSpaces(line);
        BIND ret = new BIND();
        ret.setArgs(line.split("="));
        return ret;


    }

    private String firstWord(String line) {
        return line.split(" ", 2)[0];
    }

    private String removeFirstWord(String line) {
        return line.split(" ", 2)[1];
    }

    private String clearWhiteSpaces(String line) {
        return line.replace(" ", "");
    }
}
