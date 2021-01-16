package Interpreter.Commands.Fundation;

import Interpreter.Commands.Exceptions.CommandNotFoundException;
import Interpreter.Commands.util.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandTranslator {
    private static CommandTranslator instance;

    private Map<String, Class<? extends  Command<?>>> commands;


    private CommandTranslator() {
        this.commands = new HashMap<>();
        this.commands.put("return", RETURN.class);
        this.commands.put("var", CreateVariableCommand.class);
        this.commands.put("while",WHILE.class);
        this.commands.put("if", IF.class);
        this.commands.put("openDataServer", OPENDATASERVER.class);
        this.commands.put("connect", CONNECT.class);
        this.commands.put("disconnect", DISCONNECT.class);
        this.commands.put("sleep", SLEEP.class);
        this.commands.put("print", PRINT.class);

    }

    public static CommandTranslator getInstance() {
        if(instance == null) {
            instance = new CommandTranslator();
        }

        return instance;
    }

    public Class<? extends Command<?>> translate(String commandName) throws CommandNotFoundException {
        commandName = commandName.replace("\t", "");

        if(this.commands.containsKey(commandName)){
            return commands.get(commandName);
        }
        else if(tryParseDouble(commandName)){
            return NUMBER.class;
        }else if (commandName.isEmpty()){
            return NOP.class;
        }
        throw new CommandNotFoundException("Command: " + commandName + " is not a valid command");
    }

    public Collection<Class<? extends Command<?>>> allCommands() {
        return commands.values();
    }

    public void addCommand(String key, Class<? extends Command<?>> cmd) {
        this.commands.put(key, cmd);
    }

    private boolean tryParseDouble(String s) {
        try{
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}