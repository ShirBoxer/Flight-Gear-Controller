package model;
import javafx.beans.property.SimpleDoubleProperty;

public class AssignCommand implements Command {

	@Override
	public double doCommand(String[] code) {

		String var = code[0];
		StringBuilder sb = new StringBuilder();
		String path;
		Double value;

		if(!MyInterpreter.symbolTable.containsKey(var)) {
			System.out.println(var + " UnDeclared");
			return -1.0;
			}

		// x = bind PATH   [x,bind,PATH,;]
		if(code[1].equals("bind")) {

		if(MyInterpreter.symbolTable.containsKey(code[0]) &&
		  (MyInterpreter.symbolTable.get(code[0]).doubleValue() != -1))
					return 0.0;


				for(int i = 2; i< code.length-1; i++) {//concat path
					if(code[i].equals("\"")) continue;
					sb.append(code[i]);
				}
				sb.deleteCharAt(sb.length()-1);
				path = sb.toString();

				//binding between var and path with SimpleDoubleProperty Class
				MyInterpreter.symbolTable.put(var, new SimpleDoubleProperty(0.0));
				MyInterpreter.pathToValue.put(path, new SimpleDoubleProperty(0.0));
				MyInterpreter.symbolTable.get(var)
				.bindBidirectional(MyInterpreter.pathToValue.get(path));
				MyInterpreter.varToPath.put(var, path);
		}
		else{ // regular assign like " x = 5 + y " [x,5,+,y,;]
			for(int i = 1; i< code.length-1; i++)//concat mathematics equation.
				sb.append(code[i]);
			value = ShuntingYard.calc(sb.toString());
			MyInterpreter.symbolTable.get(var).set(value);
			path = MyInterpreter.varToPath.get(var);
			//writing to flight gear simulator.
			ConnectCommand.out.println("set " + path + " " + value);
			}

		return 0.0;
	}

}
