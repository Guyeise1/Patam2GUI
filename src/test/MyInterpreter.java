package test;

import Interpreter.Commands.Fundation.CodeBlock;
import Interpreter.Commands.util.DISCONNECT;
import Interpreter.Variables.Fundation.VariableManager;
import Interpreter.Variables.Fundation.VariableProvider;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class MyInterpreter {

	VariableManager variableManager;

	private MyInterpreter() {
		variableManager = VariableProvider.getInstance();
	}

	private final static MyInterpreter instance = new MyInterpreter();

	public static MyInterpreter getInstance() {
		return instance;
	}

	public static final int DEFAULT_RETURN_STATUS = 0;

	public String assignVariableValues(String command) {
		String replacedVariableWithValueCommand = command;
		for (String variableName : variableManager.getAll()) {
			Double value = variableManager.getValue(variableName);
			if (value != null) {
				replacedVariableWithValueCommand =
						replacedVariableWithValueCommand.replace(variableName, String.valueOf(value));
			}
		}
		return replacedVariableWithValueCommand;
	}

	public VariableManager getVariablesFactory() {
		return variableManager;
	}

	public static int interpret(String[] lines){
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getInstance().variableManager.clear();
		String code = String.join("\n", lines);
		CodeBlock cb = new CodeBlock(code);
		try {
			return cb.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
}
