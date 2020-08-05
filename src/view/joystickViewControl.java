package view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import model.ConnectCommand;
import model.*;

public class joystickViewControl {

	//vars
	public DoubleProperty smallCircleX;
	public DoubleProperty smallCircleY;
	public DoubleProperty rudder;
	public DoubleProperty throttle;


	public joystickViewControl() {
		smallCircleX = new SimpleDoubleProperty();
		smallCircleY = new SimpleDoubleProperty();
		rudder = new SimpleDoubleProperty();
		throttle = new SimpleDoubleProperty();


	}

	public void joystickCenter(){
		smallCircleX.set(0);
		smallCircleY.set(0);

	}

	public void joystickMovement() {
		String aPath = new String(MyInterpreter.varToPath.get("aileron"));
		String ePath = new String(MyInterpreter.varToPath.get("elevator"));
		double x = norma(smallCircleX.get());
		double y = norma(smallCircleY.get());
		ConnectCommand.out.println("set "+ aPath + " " + x);
		ConnectCommand.out.println("set "+ ePath + " " + y);


	}

	public double norma(double num){
		if(num>=30)	return 1;
		if(num<= -30) return -1;
		return num/30;
	}



	public void moveRudder() {
		String path = new String(MyInterpreter.varToPath.get("rudder"));
		ConnectCommand.out.println("set "+ path + " " + rudder.get());

	}

	public void moveThrottle() {
		String path = new String(MyInterpreter.varToPath.get("throttle"));
		ConnectCommand.out.println("set "+ path + " " + throttle.get());

	}
}
