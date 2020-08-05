package model;


public class returnCommand implements Command {

	@Override
	public double doCommand(String[] code) {

		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < code.length -1 ; i++)
			sb.append(code[i]);

		double d =  ShuntingYard.calc(sb.toString());

		// cleaning resources when function end.

		if(!MyInterpreter.varToPath.isEmpty())
			MyInterpreter.varToPath.clear();

		if(!MyInterpreter.symbolTable.isEmpty())
			MyInterpreter.symbolTable.clear();

		if(!MyInterpreter.pathToValue.isEmpty())
			MyInterpreter.pathToValue.clear();

		if(!MyInterpreter.varList.isEmpty())
			MyInterpreter.varList.clear();
		return d;

		}
	}


