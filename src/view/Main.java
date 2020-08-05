package view;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import viewModel.ViewModel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {


	@Override
	public void start(Stage primaryStage) {
		try {

			try {
				FXMLLoader fxl = new FXMLLoader();
				BorderPane root = fxl.load(getClass().getResource("MainWindow.fxml").openStream());
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);

				MainWindowController mwc = fxl.getController(); // View mwc.setViewModel(vm); vm.addObserver(mwc);
				Model m = new Model();  // Model
				mapView mv = new mapView(mwc.mapCanvas.getGraphicsContext2D(),
										 mwc.pathCanvas.getGraphicsContext2D(),
										 mwc.airplanePic,
										 mwc.xPic,
										 mwc.mapCanvas.getWidth(),
										 mwc.mapCanvas.getHeight()
										);
				joystickViewControl jsc = new joystickViewControl();
				ViewModel vm = new ViewModel(m,mv,jsc); // View-Model m.addObserver(vm);
				m.addObserver(vm);
				vm.addObserver(mwc);
				mwc.setViewModel(vm);
				primaryStage.show();

			} catch(Exception e) {
				e.printStackTrace();
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
