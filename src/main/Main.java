package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application{

	private static AnchorPane joinPane, gamePane;
	private static Scene scene;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		loadJoin(stage);
	}

	private void loadJoin(Stage stage) {
		String fxmlName = "/join.fxml";
		
		try{
			joinPane = FXMLLoader.load(getClass().getResource(fxmlName));
		} catch (Exception e){
			e.printStackTrace();
			System.exit(-1);
		}

		scene = new Scene(joinPane);
		stage.setScene(scene);
		stage.setTitle("Create or join a game of Snake!");
		stage.setResizable(false);
		stage.centerOnScreen();
		stage.show();
	}

}
