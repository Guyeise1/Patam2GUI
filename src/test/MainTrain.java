package test;

import Interpreter.Commands.Exceptions.*;
import Interpreter.Commands.Fundation.CodeBlock;
import Interpreter.Commands.Fundation.Command;
import Interpreter.Commands.Fundation.ConditionalCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class MainTrain {

	public static void main(String[] args) {
		try {
			prodMain(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void prodMain(String[] args) {
		Random r=new Random();
		int port=r.nextInt(1001)+5000;
		Simulator sim=new Simulator(port); // sim_client on port+1, sim_server on port
		
		int rand=r.nextInt(1000);

		String[] test1={
				"return "+rand+" * 5 - (8+2)"	
		};
		
		if(MyInterpreter.interpret(test1)!=rand*5-(8+2))
			System.out.println("failed test1 (-20)");
		else
			System.out.println("passed test 1");

		String[] test2={
				"var x",	
				"x="+rand,	
				"var y=x+3",	
				"return y"	
		};

		if(MyInterpreter.interpret(test2)!=rand+3)
			System.out.println("failed test2 (-20)");
		else
			System.out.println("passed test 2");
		String[] test3={
				"openDataServer "+(port+1)+" 10",
				"connect 127.0.0.1 "+port,
				"var x",
				"x = bind simX",
				"var y = bind simX",
				"x = "+rand*2,
				"disconnect",
				"return y"
		};

		int weGot = MyInterpreter.interpret(test3);
		System.out.println("we Got : " +weGot);
		if(weGot!=rand*2)
			System.out.println("failed test3 (-20)");
		else
			System.out.println("passed test 3");
		System.out.println("simX: "+sim.simX+ " simY: "+sim.simY+ " simZ: "+sim.simZ);
		String[] test4={
				"openDataServer "+ (port+1)+" 10",
				"connect 127.0.0.1 "+port,
				"var x = bind simX",
				"var y = bind simY",	
				"var z = bind simZ",	
				"x = "+rand*2,
				"disconnect",
				"return x+y*z"	
		};

		weGot = MyInterpreter.interpret(test4);
		System.out.println("We got: " + weGot);
		System.out.println("He got: " + String.valueOf(sim.simX+sim.simY*sim.simZ));
		if(weGot!=sim.simX+sim.simY*sim.simZ)
			System.out.println("failed test4 (-20)");
		else
			System.out.println("passed test 4");

		String[] test5={
				"var x = 0",
				"var y = "+rand,
				"while x < 5 {",
				"	y = y + 2",
				"	x = x + 1",
				"}",
				"return y"	
		};
		
		if(MyInterpreter.interpret(test5)!=rand+2*5)
			System.out.println("failed test5 (-20)");
		else
			System.out.println("passed test 5");

		sim.close();
		System.out.println("done");
	}


	private static void testCode() throws Exception {
		String[] code = new String[] { "var x=1","var sum = 0", "while x <= 100 {" , "	sum=sum+x", "	x=x+1",  "}","return sum"};

		System.out.println(MyInterpreter.interpret(code));

	}

}
