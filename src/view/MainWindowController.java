package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import viewModel.ViewModel;

public class MainWindowController implements Observer,AutoCloseable{
	@FXML
	Canvas mapCanvas;
	@FXML
	Canvas 	pathCanvas;
	@FXML
	ImageView airplanePic;
	@FXML
	ImageView xPic;
	@FXML
	Circle smallCircle;
	@FXML
	Circle bigCircle;
	@FXML
	Slider rudderSlider;
	@FXML
	Slider throttleSlider;
	@FXML
	Button connectB;
	@FXML
	Button loadDataB;
	@FXML
	Button calculateB;
	@FXML
	Button loadCodeB;
	@FXML
	RadioButton manualB;
	@FXML
	RadioButton autopilotB;
	@FXML
	ToggleGroup tg;
	@FXML
	TextArea autoPcode;

	ViewModel vm;

	//Vars
	private boolean isCalculated;
	private boolean xDrew;
	private boolean mapLoaded;
	private double lastLegalX;
	private double lastLegalY;


	public void connect(){

		// opening new dialog window for getting connection details (ip , port).
				Stage newStage = new Stage();
				VBox comp = new VBox();
				Button b = new Button("connect");
				TextField ipField = new TextField("IP");
				TextField portField = new TextField("PORT");
				comp.getChildren().add(ipField);
				comp.getChildren().add(portField);
				comp.getChildren().add(b);

				Scene stageScene = new Scene(comp);
				newStage.setScene(stageScene);
				newStage.show();
				// dialog presented.

				vm.ip.bind(ipField.textProperty());
				vm.port.bind(portField.textProperty());


				b.setOnMouseClicked((e)->{
					newStage.close();
					vm.connect();
					});
	}


	// load CSV
	public void loadData(){
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		fc.setTitle("Select CSV file");

		//FileChooser.ExtensionFilter extFilter1 = new FileChooser.ExtensionFilter("CSV files (.csv)", ".csv");
		//fc.getExtensionFilters().add(extFilter1);

		File choosenFile = fc.showOpenDialog(null);

		if(choosenFile != null){
			mapLoaded = true;
			vm.loadData(choosenFile); // NEED TO FIND BETTER SOLUTION (MAYBE BIND)
			if(!airplanePic.isVisible()) airplanePic.setVisible(true);

		}

	}

	public void drawX(MouseEvent me){
		xDrew = true;
		if(!mapLoaded) return;
		xPic.setX(me.getX()-this.xPic.getFitWidth()/2);
		xPic.setY(me.getY()-this.xPic.getFitHeight()/2);
		if(!xPic.isVisible()) xPic.setVisible(true);
		if(isCalculated)calcPath();

	}

	public void calcPath(){
		//TODO: check that X and the map is existed.
		if(!xDrew) return;

		if(isCalculated == false){ isCalculated = true;
					//creating pop-up window as required
					Stage newStage = new Stage();
					TextArea txt = new TextArea();
					Button b = new Button("Exit");
					b.setOnMouseClicked((e)->newStage.close());

					VBox vb = new VBox();
					txt.appendText("Connecting to the server \n"
								+ "IP: 127.0.0.1 \n"
								+ "PORT: 37543 \n");
					vb.getChildren().add(txt);
					vb.getChildren().add(b);
					Scene stageScene = new Scene(vb);
					newStage.setScene(stageScene);
					newStage.show();
		}
				vm.drawPath();
	}

	public void loadCode(){
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		fc.setTitle("Select txt file");
		File choosenFile = fc.showOpenDialog(null);

		try {
			BufferedReader buf = new BufferedReader(new FileReader(choosenFile));
			String line;
			while((line = buf.readLine())!= null)
				autoPcode.appendText(line + "\n");
			buf.close();
		} catch ( IOException e) {e.printStackTrace();}


	}

	public void autopilot(){
		if(autoPcode.getText().isEmpty()){
			//textArea.setFont(new Font(text, 0, ));
			autoPcode.textProperty().set("Please Enter Code before trying Autopilot functionallity.");
			return;
			}
		vm.interpret();
	}

	public void joystcikMovement(MouseEvent me){
		if(!manualB.isSelected()) return; //TO DO - msg to client
		double x,y;
		x= me.getX();
		y= me.getY();

		if(x>30 || x<-30 || y>30 || y <-30)
		{
			smallCircle.setCenterX(lastLegalX);
			smallCircle.setCenterY(lastLegalY);
		}
		else{
		lastLegalX = x;
		lastLegalY=y;
		smallCircle.setCenterX(x);
		smallCircle.setCenterY(y);
		vm.joystickMovement();
		}
	}

	public void joystickRelease(){
		vm.joystickControl.joystickCenter();
	}

	public void rudder(){
		if(!manualB.isSelected()) return; //TO DO - msg to client
		vm.joystickControl.moveRudder();

	}

	public void throttle(){
		if(!manualB.isSelected()) return; //TO DO - msg to client
		vm.joystickControl.moveThrottle();
	}



	public void interpret(){
		if(autoPcode.getText().isEmpty()){
			//textArea.setFont(new Font(text, 0, ));
			autoPcode.textProperty().set("Please Enter Code before trying Autopilot functionallity.");
			return;
			}
		vm.interpret();
	}


	@Override
	public void update(Observable arg0, Object arg1) {

	}

	public void setViewModel(ViewModel vm2) {
		vm = vm2;
		isCalculated = false;
		mapLoaded = false;
		xDrew = false;
		xPic.setImage(new Image("file:RES/xPic.png"));
		airplanePic.setImage(new Image("file:RES/airplane.png"));
		vm.joystickControl.smallCircleX.bindBidirectional(smallCircle.centerXProperty());
		vm.joystickControl.smallCircleY.bindBidirectional(smallCircle.centerYProperty());
		vm.mapControl.getDestX().bind(this.xPic.xProperty());
		vm.mapControl.getDestY().bind(this.xPic.yProperty());
		airplanePic.xProperty().bind(vm.mapControl.getCanvasPosX());
		airplanePic.yProperty().bind(vm.mapControl.getCanvasPosY());
		vm.getCode().bind(autoPcode.textProperty());
		vm.joystickControl.rudder.bind(rudderSlider.valueProperty());
		vm.joystickControl.throttle.bind(throttleSlider.valueProperty());

	}

	@Override
	public void close() throws Exception {
		vm.mapControl.setStop(true);

	}




}
