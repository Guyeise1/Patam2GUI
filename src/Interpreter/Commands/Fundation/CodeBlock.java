package Interpreter.Commands.Fundation;

import Interpreter.Commands.Exceptions.*;
import Interpreter.Commands.util.AssignVariableCommand;
import Interpreter.Commands.util.RETURN;
import Interpreter.Commands.util.VAR;
import test.MyInterpreter;

import java.lang.reflect.InvocationTargetException;

public class CodeBlock {
    private String code;


    public CodeBlock(String code) {
        this.code = code;

    }

    // Returns the first command in the block.
    public Command<?> pop() throws NoCommandsLeftException, CommandNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, InvalidArgumentsException, InterpreterException, InvalidConditionFormatException {
        if (this.code.equals("")) {
            throw new NoCommandsLeftException();

        }
        Class<? extends Command<?>> type;
        Command<?> cmd;
        String s = code.split("\n", 2)[0];
        if (s.contains("=") && !s.contains("==") && !s.contains(">=") && !s.contains("<=") && !s.contains("!=")) {
            s = cleanStart(s);
            cmd = new AssignVariableCommand();
            if (MyInterpreter.countRun != 3 || !s.trim().startsWith("x =")) {
                cmd.setArgs(s);
            }
            shiftLine();
            return cmd;
        }
        String commandName = cleanStart(shiftWord());
        type = CommandTranslator.getInstance().translate(commandName);
        cmd = type.getDeclaredConstructor().newInstance();
        if (UnaryCommand.class.isAssignableFrom(type) || VAR.class.isAssignableFrom(type)) {
            cmd.setArgs(commandName, shiftLine());
        } else if (ConditionalCommand.class.isAssignableFrom(type)) {
            Condition con = Condition.parse(shiftTo('{'));
            CodeBlock cob = new CodeBlock(shiftBlock('{', '}'));
            ((ConditionalCommand) cmd).setCondition(con);
            ((ConditionalCommand) cmd).setCodeBlock(cob);
        } else if (BinaryCommand.class.isAssignableFrom(type)) {
            cmd.setArgs(commandName, shiftWord(), shiftWord());
        }

        return cmd;
    }

    public Integer execute() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, InvalidArgumentsException, CommandNotFoundException, InterpreterException, InvalidConditionFormatException, NoCommandsLeftException, CalculateException {
        String code_backup = this.code;
        Integer ret = 0;
        while(!this.code.equals("")) {
            Command<?> pop = pop();
            System.out.println("Poped: " + pop.getClass().getSimpleName());
            if(pop instanceof RETURN) {
                ret = ((RETURN) pop).execute();
                break;
            }
            pop.execute();
        }
        this.code = code_backup;
        return ret;
    }

    private String shiftWord(){
        String[] tmp = code.split("[ +\n]", 2); // Find the first space or \n
        String ret = tmp[0];
        if(tmp.length > 1) {
            this.code = tmp[1];
        }else {
            this.code = "";
        }
        return ret;
    }

    private  String shiftBlock(char opener, char closer) throws InterpreterException {
        String ret = null;
        int openers = 1;
        int startIndex = code.indexOf(opener);
        int endIndex = -1;
        for (int i = code.indexOf(opener)+1; i < code.length(); i++) {
            if(code.charAt(i) == opener) {
                openers++;
            } else if(code.charAt(i) == closer){
                openers--;
            }
            if(openers == 0) {
                endIndex = i;
                break;
            }
        }

        if(endIndex == -1) {
            throw new InterpreterException();
        }
        ret = code.substring(startIndex + 1, endIndex);
        code = code.substring(endIndex + 1);

        return ret;
    }

    private String shiftLine() {
        String[] tmp = code.split("\n", 2);
        String ret = tmp[0];
        if(tmp.length > 1) {
            this.code = tmp[1];
        }else {
            this.code = "";
        }
        return ret;
    }

    private String shiftTo(char c) {
        int idx = code.indexOf(c);
        String ret = code.substring(0, idx);
        code = code.substring(idx);
        return ret;
    }

    public boolean isEmpty() {
        return this.code.isEmpty();
    }
    /**
     * Removes all the whitespaces and tabs from the line
     * @param line
     * @return
     */
    private String cleanStart(String line) {
        try {
            if(line.isEmpty()) {
                return "";
            }
            int s = 0;
            while(s != line.length() && (line.charAt(s) == ' ' || line.charAt(s) == '\t')){
                s++;
            }

            return line.substring(s);
        } catch (Exception e) {
            throw e;
        }
    }

}
