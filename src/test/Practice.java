package test;

import Interpreter.Commands.Exceptions.CalculateException;
import Interpreter.Commands.Exceptions.InvalidConditionFormatException;
import Interpreter.Commands.Exceptions.ParseException;
import Interpreter.Commands.Fundation.Condition;

public class Practice {
    public static void main(String[] args) throws InvalidConditionFormatException, ParseException, CalculateException {
        Condition con = Condition.parse("5+3 != 5+2");
        System.out.println(con.calculate());
    }
}
