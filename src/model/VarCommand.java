package model;

import javafx.beans.property.SimpleDoubleProperty;

public class VarCommand implements Command {

	@Override
	public double doCommand(String[] code) {

		if(MyInterpreter.symbolTable.containsKey(code[0])){
			return 0.0;
		}
		if(code.length != 2 || MyInterpreter.symbolTable.containsKey(code[0])) {
			System.out.println("problem with var command");
			return -1.0;
		}

		MyInterpreter.symbolTable.put(code[0], new SimpleDoubleProperty(-1.0)); // (value = -1) flag for AssignCommand
		return 0.0;
	}

}
