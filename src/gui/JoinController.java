package gui;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.ResourceBundle;

import dto.GameDto;
import javafx.event.ActionEvent;
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
import javafx.scene.paint.Color;
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
	private ComboBox<String> playerNbrComboBox;
	
	private static final String AI_ONLY = "AI only";

	@FXML
	void initialize() {
		errorLbl.setText("");
		errorLbl.setStyle("-fx-text-fill: red;");
		Locale.setDefault(Locale.ENGLISH);

		
		playerNbrComboBox.getItems().clear();;
		
		for(int i = 1; i <= 4; i++){
			playerNbrComboBox.getItems().add("" + i);
		}
		
		playerNbrComboBox.getItems().add(AI_ONLY);
		playerNbrComboBox.getSelectionModel().selectFirst();
	}

	@FXML
	private void onCreateClick() {
		loadGame(true);
	}

	@FXML
	private void onJoinClick() {    	
		loadGame(false);
	}

	private void loadGame(boolean host){
		GameDto info = null; //TODO handle null

		try {
			info = getGameInfo(host);
		} catch (UnknownHostException e) {
			errorLbl.setText("Not a valid IP address!");
			return;
		}

		//TODO connect to IP given (not host) or create a game before loading the gamePane (host)
		//TODO give info to some connecting static class? later: server

		Parent gamePane;
		try {
			gamePane = FXMLLoader.load(getClass().getResource("/game.fxml"));
		} catch (IOException e) {
			errorLbl.setText("Could not load game!");
			//e.printStackTrace();
			return;
		}

		Scene scene = new Scene(gamePane);
		Stage stage = (Stage) ((Node) joinPane).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

	private GameDto getGameInfo(boolean host) throws UnknownHostException {
		Color color = colorPicker.getValue();
		String name = nameTxt.getText();
		InetAddress ip;
		int players;

		if(!host){
			ip = InetAddress.getByName(ipTxt.getText());
			return new GameDto(name, color, ip);
		}
		else{
			String item = playerNbrComboBox.getSelectionModel().getSelectedItem();
			if(item.equals(AI_ONLY)){
				players = 0;
				color = null;
			}
			else{
				players = Integer.parseInt(item);
			}
			return new GameDto(name, color, players);
		}
	}

}
