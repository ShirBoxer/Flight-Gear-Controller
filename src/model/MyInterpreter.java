package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class MyInterpreter {

    public static HashMap<String, String> varToPath = new HashMap<>();
    public static HashMap<String, DoubleProperty> pathToValue = new HashMap<>();
	public static HashMap<String, DoubleProperty> symbolTable= new HashMap<>();
	public static  HashMap<String,Command> commandMap = new HashMap<>();
	public static ArrayList<String> varList = new ArrayList<>();

	//	static initialize
	static {
		//creating Commands for commandMap
		commandMap.put("=" ,			 new AssignCommand());
		commandMap.put("openDataServer", new odsCommand());
		commandMap.put("var" ,			 new VarCommand());
		commandMap.put("connect",		 new ConnectCommand());
		commandMap.put("while",	 		 new LoopCommand());
		commandMap.put("sleep", 		 new SleepCommand());
		commandMap.put("disconnect",	 new DisconnectCommand());
		commandMap.put("return", 		 new returnCommand());
		commandMap.put("print",			 new PrintCommand());


		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("RES\\var_Names.txt")));
			String line,name,path;
			String[] nameNpath;
			while((line = br.readLine()) != null){
				nameNpath = line.split(",");
				// NAME , PATH
				name = nameNpath[0];
				path = nameNpath[1];
				symbolTable.put(name, new SimpleDoubleProperty(0.0));
				pathToValue.put(path, new SimpleDoubleProperty(0.0));
				symbolTable.get(name).bindBidirectional(pathToValue.get(path));
				varToPath.put(name, path);
				varList.add(name);

			}
			br.close();
		} catch (IOException e) {e.printStackTrace();}


	}
	public static  double interpret(String[] lines){
		return parser(lexer(lines));

	}

	public static double parser(ArrayList<String> byteCode) {
		String word;
		String[] command;
		double retVal = 0;

		for(int i = 0 ; i<byteCode.size(); i++) {
			word = byteCode.get(i);
			if(commandMap.containsKey(word)) {	//if 'word' is keyword ?
					command = getNextCommands(i,byteCode); // get all next words until ';'. and add i increment.
					i = Integer.parseInt(command[command.length-1]);// 'getNextCommands return new 'i'
					command[command.length-1] = ";";// End-of-line symbol (command-end)
					retVal = commandMap.get(word).doCommand(command);// calling specific doCommand With the required parameters.
			}
		}
		return retVal;
	}

	public static String[] getNextCommands(int index,ArrayList<String> byteCode) {
		int i = index + 1; // byteCode[index] is the command
		ArrayList<String> cmd = new ArrayList<>();

		switch(byteCode.get(index)) {

			case "disconnect":
				break;
			//########################################################
			case "openDataServer":
					while(!byteCode.get(i).equals(";"))
						cmd.add(byteCode.get(i++));
				break;
			//########################################################
			case "connect":
				while(!byteCode.get(i).equals(";"))
					cmd.add(byteCode.get(i++));
				break;

			//########################################################

			case "print":
				while(!byteCode.get(i).equals(";"))
					cmd.add(byteCode.get(i++));
				break;
			//########################################################

			case "return":
				while(!byteCode.get(i).equals(";"))
					cmd.add(byteCode.get(i++));
				break;
			//########################################################

			case "sleep":
				while(!byteCode.get(i).equals(";"))
					cmd.add(byteCode.get(i++));
				break;
			//########################################################

			case "var":
				cmd.add(byteCode.get(i));
				break;
			//########################################################

			case "=":
				cmd.add(byteCode.get(index-1));
				while(!byteCode.get(i).equals(";"))
					cmd.add(byteCode.get(i++));
				break;
			//########################################################

			case "while":
				while(!byteCode.get(i).equals("}"))
					cmd.add(byteCode.get(i++));
				cmd.add(byteCode.get(i));
				break;
			//########################################################

		}


		String[] commands = new String[cmd.size()+1];
		commands[cmd.size()] = i+""; // for updating index in parser loop.
		for(int j = 0 ; j < cmd.size(); j++)
			commands[j] = cmd.get(j);

		return commands;
	}


	public static ArrayList<String> lexer(String[] code) {
		ArrayList<String> byteCode = new ArrayList<>();
		String[] temp;
		for(String line : code) {
			// split string by 'space' character
			temp = line.split("[ ]+");

			byteCode.addAll(breakToTokens(temp));
			byteCode.add(";");
		}
		return byteCode;
    }


    public static ArrayList<String> breakToTokens(String[] str) {
	ArrayList<String> tokens = new ArrayList<>();

	String[] temp;
	for(String s : str) {
		temp = s.split("(?<=[-+*/()=])|(?=[-+*/()=])");
		for(int i = 0 ; i < temp.length; i++) {
				temp[i]= temp[i].trim(); //removes all leading & trailing spaces
				tokens.add(temp[i]);  // adding the words to tokens arrayList
	    }
	}
	//Removing 'Empty' OR 'Space' nodes.
	tokens.removeIf((s)->(s.equals(" ")|| s.equals("")));
	return tokens;
	}


}