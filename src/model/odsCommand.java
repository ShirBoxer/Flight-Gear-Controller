package model;

public class odsCommand implements Command {
	public DataReaderServer drs;
	@Override
	public double doCommand(String[] code) {// [ PORT , frequency , ;]
		if(code.length != 3) {
			System.out.println("wrong script for ODScommand");
			return 0.0;
		}
		int port = Integer.parseInt(code[0]);
		int frequency = Integer.parseInt(code[1]);
		new Thread(()->{
			drs = new DataReaderServer(port,frequency);
			drs.runServer();
		}).start();
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}


		return 0.0;
	}

}
