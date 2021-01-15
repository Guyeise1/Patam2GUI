package Interpreter.Commands.Fundation;

import Interpreter.CalcExpresion;
import Interpreter.Commands.Exceptions.CalculateException;
import Interpreter.Commands.Exceptions.InvalidConditionFormatException;
import Interpreter.Commands.Exceptions.ParseException;
import Interpreter.Expression.Expression;
import test.MyInterpreter;

public class Condition {
    private String text;

    private String left;
    private String right;
    private Operator operator;

    public Boolean calculate() throws ParseException, CalculateException {
        double c_left = assignExpression(left).calculate();
        double c_right = assignExpression(right).calculate();
        switch (operator) {
            case GT:
                return c_left > c_right;
            case LT :
                return c_left < c_right;
            case EQ:
                return c_left == c_right;
            case NE:
                return c_left != c_right;
            case GE:
                return c_left >= c_right;
            case LE:
                return c_left <= c_right;
        };

        throw new CalculateException();
    }

    /**
     *
     * @param text - 3 + 2 < 6
     * @return
     * @throws InvalidConditionFormatException
     * @throws ParseException
     */
    public static Condition parse(String text) throws InvalidConditionFormatException, ParseException {
        Operator operator  = null;
        for (Operator opr: Operator.values() ) {
            if(text.contains(opr.value)) {
                operator = opr;
                break;
            }
        }

        if(operator == null) {
            throw new InvalidConditionFormatException();
        }

        String[] expression = text.split(operator.getValue());

        Condition ret = new Condition();
        ret.left = expression[0];
        // ret.left = CalcExpresion.parseExpression(expression[0]);
        ret.right = expression[1];
        ret.operator = operator;

        return ret;
    }

    public String getText(){
        return this.text;
    }
    /**
     *
     * @param txt - (x + 3) * 2
     * @return - ( 5 + 3 ) * 2
     */
    private static Expression assignExpression(String txt) throws ParseException {
        String simpleExpression = MyInterpreter.getInstance().assignVariableValues(txt); // without variables (after assign)
        return CalcExpresion.parseExpression(simpleExpression);

    }

    // The operators are sorted meaning
    // you don't have to worry about
    // find '<' before '<='.
    private enum Operator {
        GE(">="),
        LE("<="),
        EQ("=="),
        NE("!="),
        GT(">"),
        LT("<");


        private final String value;
        Operator(String s) {
            this.value = s;
        }

        public String getValue() {
            return this.value;
        }

        public boolean ContaintsEQUALS() {
            return value.contains("=");
        }
    }
    
}
