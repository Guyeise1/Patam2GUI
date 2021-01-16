package test;

import Interpreter.Commands.Fundation.CodeBlock;
import Interpreter.Commands.Fundation.Variables;
import Interpreter.Commands.Fundation.VariablesFactory;
import Interpreter.Commands.util.DISCONNECT;

import java.util.stream.Stream;

public class MyInterpreter {

	Variables variables;

	private MyInterpreter() {
		variables = VariablesFactory.getInstance();
	}

	private static MyInterpreter instance = new MyInterpreter();

	public static MyInterpreter getInstance() {
		return instance;
	}

	public static final int DEFAULT_RETURN_STATUS = 0;

	public String assignVariableValues(String command) {
		String replacedVariableWithValueCommand = command;
		for (String variableName : variables.allVariableNames()) {
			Double value = variables.getValue(variableName);
			while(variables.containsVariable(variableName) && value == null && command.contains(variableName)) {
				try {
					Thread.sleep(100);
					value = variables.getValue(variableName);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(value != null) {
				replacedVariableWithValueCommand = replacedVariableWithValueCommand.replace(variableName, String.valueOf(value));
			}
		}
		return replacedVariableWithValueCommand;
	}

	public String[] assignVariableValues(String... commands) {
		return Stream.of(commands)
				.map(this::assignVariableValues)
				.toArray(String[]::new);

	}

	public Variables getVariablesFactory() {
		return variables;
	}

	public static int countRun = 0;
	public static int interpret(String[] lines){
		countRun ++;
		getInstance().variables.clean();
		try {
			new DISCONNECT().execute();
		} catch (Exception ignored) {}
		String code = String.join("\n", lines);
		System.out.println("test #" + countRun + " code for run is:\n " + code);
		CodeBlock cb = new CodeBlock(code);
		try {
			return cb.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
}
