package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import dto.DataTransferrer;
import dto.GameDto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

public class GameController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane gamePane, playPane, player1Pane, player2Pane, player3Pane, player4Pane;

	@FXML
	private Label player1Lbl, player2Lbl, player3Lbl, player4Lbl;

	@FXML
	private Button disconnectBtn, readyBtn;

	@FXML
	private TextArea msgArea;

	private GameDto info;
	public final static double RANGE_VALUE = .12; //percentage on the color scale that we want to be off, at least (needs to be at most 1/6 (=0.166..), otw can't find 3 other colors)

	@FXML
	public void initialize() {
		msgArea.setText("");

		info = DataTransferrer.getInfo();

		if(info == null){
			//TODO error of some sort
		}
		else{
			setPlayerStyle(1,info.getName(),info.getColor());
		}
		
		assignAIColors();		
		
		//TODO initialize with info from network/joincontroller

		//TODO place ready button in your own pane, create ready indicators for all players

		//TODO if host create a start game button (right of disconnect button? disabled until all are ready, function for re-disabling necessary)
	}

	//TODO add start button for host, additional information (health ...)

	private void setPlayerStyle(int player, String name, Color color) { //TODO expand upon
		String style = "-fx-background-color: #" + Integer.toHexString(color.hashCode()) + ";"; //TODO, can, in rare cases produce colors with 7 hex values Oo (e.g. #96b25ff), seems to happen if 'ff' occurs

		System.out.println(name);
		
		if(player == 1){
			player1Lbl.setText(name);
			player1Pane.setStyle(style);
		}
		else if(player == 2){
			player2Lbl.setText(name);
			player2Pane.setStyle(style);
		}
		else if(player == 3){
			player3Lbl.setText(name);
			player3Pane.setStyle(style);
		}
		else if(player == 4){
			player4Lbl.setText(name);
			player4Pane.setStyle(style);
		}
		else{
			//TODO some error
		}
	}

	@FXML
	private void onDisconnectClick(){
		//TODO send info to other clients (host/other difference)
		//TODO clean up server/client

		showJoin();
	}

	@FXML
	private void onReadyClick(){
		//TODO change text in button, send ready info (--> green checkmark or something)
	}

	private void addMsg(String msg) {
		msgArea.setScrollTop(Double.MAX_VALUE); //scrolls down
		msgArea.appendText("\n" + msg);
	}

	private void showJoin() {
		Parent joinPane;
		try {
			joinPane = FXMLLoader.load(getClass().getResource("/join.fxml"));
		} catch (IOException e) {
			addMsg("Load menu failed, please restart game!");
			return;
		}
		Scene scene = new Scene(joinPane);
		Stage stage = (Stage) ((Node) gamePane).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

	//TODO move that to server
	private void assignAIColors(){
		System.out.println(info.getPlayers());
		Color[] reserved = new Color[info.getPlayers()]; //currently sets null for clients, but sets correct number for host

		reserved[0] = info.getColor(); //TODO integrate into loop as soon as we gather multiple players' data in server
		
		for(int i = 0; i < reserved.length; i++){
			if(reserved[i] == null){
				reserved[i] = findGoodColor(reserved);
			}

			if(i != 0){ //TODO later generalize
				setPlayerStyle(i+1,"AI " + i, reserved[i]);
			}
			
			//TODO change script color to white if illegible bc/o dark color
		}
	}

	private Color findGoodColor(Color[] reserved) {
		ArrayList<Pair<Double,Double>> redIntervals = new ArrayList<Pair<Double,Double>>(); //open intervals for red
		ArrayList<Pair<Double,Double>> greenIntervals = new ArrayList<Pair<Double,Double>>();
		ArrayList<Pair<Double,Double>> blueIntervals = new ArrayList<Pair<Double,Double>>();		

		for(Color c: reserved){
			if(c != null){
				redIntervals.add(buildInterval(c.getRed()));
				greenIntervals.add(buildInterval(c.getGreen()));
				blueIntervals.add(buildInterval(c.getBlue()));
			}
		}

		return new Color(findValidColorValue(redIntervals), findValidColorValue(blueIntervals), findValidColorValue(greenIntervals), 1);
	}

	private double findValidColorValue(ArrayList<Pair<Double, Double>> intervals) { //TODO are we fine with this maybe never terminating? (though unrealistic)
		Random r = new Random();
		double value = 0; 
		boolean valid = false;
		
		while(!valid){
			value = r.nextDouble();
			for(Pair<Double,Double> p: intervals){
				if(value < p.getKey() || value > p.getValue()){
					valid = true;
				}
				else{
					valid = false;
					break;
				}
			}
		}

		return value;
	}

	private Pair<Double,Double> buildInterval(Double value){
		double posrange = RANGE_VALUE;
		double negrange = RANGE_VALUE;

		if(value-negrange <= 0){
			negrange = value;
		}
		if(value+posrange >= 1){
			posrange = 1 - value;
		}

		return new Pair<Double,Double>(value-negrange,value+posrange);
	}

}
