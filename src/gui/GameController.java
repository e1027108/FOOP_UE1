package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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

    @FXML
    public void initialize() {
    	msgArea.setText("");
    	
    	//TODO initialize with info from network/joincontroller
    	
    	//TODO place ready button in your own pane, create ready indicators for all players
    	
    	//TODO if host create a start game button (right of disconnect button? disabled until all are ready, function for re-disabling necessary)
    }
    
    //TODO add start button for host, additional information (health ...)
    
    @FXML
    private void onDisconnectClick(){
    	//TODO send info to other clients (host/other difference)
    	//TODO clean up server/client
    	
    	showJoin();
    }

    @FXML
    private void onReadyBtn(){
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

}
