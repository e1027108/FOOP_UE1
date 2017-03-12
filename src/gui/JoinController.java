package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class JoinController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private AnchorPane joinPane;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Button createBtn, joinBtn;

    @FXML
    private TextField ipTxt, nameTxt;
    
    @FXML
    private Label errorLbl;

    @FXML
    private ComboBox<Integer> playerNbrComboBox;

    @FXML
    void initialize() {
    	errorLbl.setText("");
    	errorLbl.setStyle("-fx-text-fill: red;");
    	
    	//TODO initialize stuff
    }
    
    @FXML
    private void onCreateClick() {
    	//TODO create a game before loading the gamePane
    	loadGame();
    }

    @FXML
    private void onJoinClick() {
    	//TODO connect to IP given
    	loadGame();
    }
    
    private void loadGame(){
    	Parent gamePane;
		try {
			gamePane = FXMLLoader.load(getClass().getResource("/game.fxml"));
		} catch (IOException e) {
			errorLbl.setText("Could not load game!");
			return;
		}
    	Scene scene = new Scene(gamePane);
    	Stage stage = (Stage) ((Node) joinPane).getScene().getWindow();
    	stage.setScene(scene);
    	stage.show();
    }

}
