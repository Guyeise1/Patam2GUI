package sample.Code;

import Interpreter.Commands.Exceptions.*;
import Interpreter.Commands.Fundation.CodeBlock;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.lang.reflect.InvocationTargetException;

public class CodeController {
    @FXML
    private TextArea textAreaCode;
    private CodeBlock code;

    private void load() {
        this.code = new CodeBlock(textAreaCode.getText());
    }

    public void execute() throws CommandNotFoundException, InstantiationException, InvocationTargetException, NoSuchMethodException, InvalidArgumentsException, IllegalAccessException, CalculateException, InterpreterException, InvalidConditionFormatException, NoCommandsLeftException {
        load();
        code.execute();
    }
}
