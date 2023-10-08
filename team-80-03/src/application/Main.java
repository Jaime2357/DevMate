package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;

/**
 * This class is for setting the scene and running the DevMate application.
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {	
		//local comment test
		try {
			HBox mainBox = (HBox)FXMLLoader.load(getClass().getClassLoader().getResource("view/Main.fxml"));
			Scene scene = new Scene(mainBox);
			//scene.getStylesheets().add(getClass().getClassLoader().getResource("css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			// set reference of Hbox mainBox in the commonObjs object
			CommonObjs commonObjs = CommonObjs.getInstance();
			commonObjs.setMainBox(mainBox);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	// Launch DevMate application
	public static void main(String[] args) {
		launch(args);
	}
	
}
