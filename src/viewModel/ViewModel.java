package viewModel;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Model;
import model.MyInterpreter;
import view.joystickViewControl;
import view.mapView;

public class ViewModel extends Observable implements Observer {

	private StringProperty code;
	public StringProperty ip;
	public StringProperty port;
	public Model model;
	public File csv;
	public mapView mapControl;
	public joystickViewControl joystickControl;

	//Ctor
	public ViewModel(Model m , mapView mvc, joystickViewControl jsc){
		model = m;
		ip = new SimpleStringProperty();
		port = new SimpleStringProperty();
		setCode(new SimpleStringProperty());
		mapControl = mvc;
		joystickControl = jsc;
		mapControl.getPosX().bind(MyInterpreter.symbolTable.get("positionX"));
		mapControl.getPosY().bind(MyInterpreter.symbolTable.get("positionY"));


	}

	//methods:

	public void connect(){
		model.connect(ip,port);
	}




	@Override
	public void update(Observable o, Object arg) {
		if(arg.equals("mapVars")){
		mapControl.setMapHeights(model.getMapHeights());
		mapControl.setDatumPointX(model.getDatumPointX());
		mapControl.setDatumPointY(model.getDatumPointY());
		mapControl.setMaxHeight(model.getMaxHeight());
		mapControl.setMinHeight(model.getMinHeight());
		mapControl.setSquareMeter(model.getSquareMeter());
		mapControl.drawMap();
		}
		else if(arg.equals("solution")){
			mapControl.drawPath(model.getSolution());
		}


	}

	public void loadData(File f) {
		model.loadData(f);
	}

	public void drawPath() {
		int startX = (int) (mapControl.getCanvasPosX().get()/mapControl.getCellW());
		int startY = (int) (mapControl.getCanvasPosY().get()/mapControl.getCellH());
		int endX = (int)(mapControl.getDestX().get()/mapControl.getCellW());
		int endY = (int)(mapControl.getDestY().get()/mapControl.getCellH());
		model.drawPath(startX,startY,endX,endY);

	}

	public StringProperty getCode() {
		return code;
	}

	public void setCode(StringProperty code) {
		this.code = code;
	}

	public void interpret() {
		model.interpret(code.get());

	}

	public void joystickMovement() {
		joystickControl.joystickMovement();

	}



}
