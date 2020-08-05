package model;

public class PrintCommand implements Command {

	@Override
	public double doCommand(String[] code) { //[var/"string"/num,;]

		if(code.length < 2)
			System.out.println("Problem with print command");

		// print var from symbolTable
		if(MyInterpreter.symbolTable.containsKey(code[0]))
			System.out.println("Print: "+MyInterpreter.symbolTable.get(code[0]).get());

		// string that represent regular string or double
		else {
			if(code[0].startsWith("\"")) // the string wrapped with ""
				System.out.println(""+code[0].substring(1,code[0].length()-2));
			else
				System.out.println(""+code[0]);
		}
		return 0.0;
	}

}
