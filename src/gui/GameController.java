package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class GameController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane playPane, player1Pane, player2Pane, player3Pane, player4Pane;
    
    @FXML
    private Label player1Lbl, player2Lbl, player3Lbl, player4Lbl;

    @FXML
    void initialize() {
    	//TODO initialize with info from network/joincontroller
    }
    
    //TODO add function for being ready, start button for host, additional information (health ...)

}
