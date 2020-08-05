package model;

public class SleepCommand implements Command {

	@Override
	public double doCommand(String[] code) {

		if(code.length != 2)
			System.out.println("Problem with sleep");

		try {
			Thread.sleep(Long.parseLong(code[0]));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 0.0;
	}

}
