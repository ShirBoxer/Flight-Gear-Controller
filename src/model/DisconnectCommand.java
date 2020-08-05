package model;

import java.io.IOException;

public class DisconnectCommand implements Command {

	@Override
	public double doCommand(String[] code) {

		DataReaderServer.close();
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		ConnectCommand.out.println("bye");
		ConnectCommand.out.close();
		try {
			ConnectCommand.in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0.0;
	}

}
