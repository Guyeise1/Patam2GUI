package Interpreter;

import Interpreter.Commands.Exceptions.ParseException;
import Interpreter.Expression.Number;
import Interpreter.Expression.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class CalcExpresion {
	private static final ScriptEngineManager mgr = new ScriptEngineManager();
//	public static Expression parseExpression(String exp) throws ParseException {
//		if(exp.charAt(0) == '-') {
//			return new Number(Double.parseDouble(exp));
//		}
//		Queue<String> queue = new LinkedList<>();
//		Stack<String> stack = new Stack<>();
//		Stack<Expression> stackExp = new Stack<>();
//
//		String[] split = exp.split("(?<=[-+*/()])|(?=[-+*/()])");
//
//		try {
//			for (String s : split) {
//				if (isDouble(s)) {
//					queue.add(s);
//				} else {
//					switch (s) {
//						case "/":
//						case "*":
//						case "(":
//							stack.push(s);
//							break;
//						case "+":
//						case "-":
//							while (!stack.empty() && (!stack.peek().equals("("))) {
//								queue.add(stack.pop());
//							}
//							stack.push(s);
//							break;
//						case ")":
//							while (!stack.peek().equals("(")) {
//								queue.add(stack.pop());
//							}
//							stack.pop();
//							break;
//					}
//				}
//			}
//			while (!stack.isEmpty()) {
//				queue.add(stack.pop());
//			}
//
//			for (String str : queue) {
//				if (isDouble(str)) {
//					stackExp.push(new Number(Double.parseDouble(str)));
//				} else {
//					Expression right = stackExp.pop();
//					Expression left = stackExp.pop();
//
//					switch (str) {
//						case "/":
//							stackExp.push(new Div(left, right));
//							break;
//						case "*":
//							stackExp.push(new Mul(left, right));
//							break;
//						case "+":
//							stackExp.push(new Plus(left, right));
//							break;
//						case "-":
//							stackExp.push(new Minus(left, right));
//							break;
//					}
//				}
//			}
//
//			return stackExp.pop();
//
//		} catch ( Exception e) {
//			throw new ParseException("Impossible parse to expression: " + exp);
//		}
//	}

/*
	public static double calc (String ex)  throws ParseException{
		Expression exp = parseExpression(ex);
		return calc(exp);
	}
*/

	public static double calc(String ex) throws ParseException{
		return calc(parseExpression(ex));
	}

	public static Expression parseExpression(String s) throws ParseException {
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		try {
			Object eval = engine.eval(s);
			return new Number(Double.parseDouble(eval.toString()));

		} catch (Exception e) {
			throw new ParseException(e);
		}
	}

	public static double calc(Expression exp) {
		return Math.floor((exp.calculate() * 1000)) /1000;
	}
	
	private static boolean isDouble(String val){
		try {
		    Double.parseDouble(val);
		    return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
		
	
}
