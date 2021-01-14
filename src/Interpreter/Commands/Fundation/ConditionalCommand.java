package Interpreter.Commands.Fundation;

import Interpreter.Commands.Exceptions.InvalidArgumentsException;

public abstract class ConditionalCommand extends Command<Void> {

    private CodeBlock codeBlock;
    private Condition condition;

    public CodeBlock getCodeBlock() {
        return codeBlock;
    }

    public void setCodeBlock(CodeBlock codeBlock) {
        this.codeBlock = codeBlock;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public void setArgs(String... args) throws InvalidArgumentsException {
        throw new InvalidArgumentsException(getClass().toString() + " does not get any arguments. use setCodeBlock instead");
    }
}