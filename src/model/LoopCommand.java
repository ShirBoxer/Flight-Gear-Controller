package model;

import java.util.ArrayList;

public class LoopCommand implements Command {
	@Override
	public double doCommand(String[] code) {
		String condition = code[1];
		String var = code[0];
		int i=5; //skipping 'loop condition' and ';'
		ArrayList<String> loopLines = new ArrayList<>();
		double value = MyInterpreter.symbolTable.get(var).doubleValue();
		double operand = Double.parseDouble(code[2]);
		double returnValue = 0.0;
		  i=5; //skipping condition and ; char
		 //building loop commands.
		 while(!code[i].equals("}")) {
			 loopLines.add(code[i++]);
		 }

		 switch(condition) {
		 case "==":
			 while(value == operand) {
				 returnValue = MyInterpreter.parser(loopLines);
				 value = MyInterpreter.symbolTable.get(var).doubleValue();
			 } break;
		 case "<=":
			 while(value <= operand) {
				 returnValue = MyInterpreter.parser(loopLines);
				 value = MyInterpreter.symbolTable.get(var).doubleValue();
			 } break;
		 case ">=":
			 while(value >= operand) {
				 returnValue = MyInterpreter.parser(loopLines);
				 value = MyInterpreter.symbolTable.get(var).doubleValue();
			 } break;
		 case "<":
			 while(value < operand) {
				 returnValue = MyInterpreter.parser(loopLines);
				 value = MyInterpreter.symbolTable.get(var).doubleValue();
			 } break;
		 case ">":
			 while(value > operand) {
				 returnValue = MyInterpreter.parser(loopLines);
				 value = MyInterpreter.symbolTable.get(var).doubleValue();
			 } break;
		 case "!=":
			 while(value != operand) {
				 returnValue = MyInterpreter.parser(loopLines);
				 value = MyInterpreter.symbolTable.get(var).doubleValue();
			 } break;

		 }
		return returnValue;
	}


}
