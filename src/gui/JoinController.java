package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class JoinController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Button createBtn, joinBtn;

    @FXML
    private TextField ipTxt, nameTxt;

    @FXML
    private ComboBox<Integer> playerNbrComboBox;

    @FXML
    void initialize() {
    	//TODO initialize something?
    }
    
    @FXML
    void onCreateClick() {
    	//TODO create game (that others are able to join), show game screen
    }

    @FXML
    void onJoinClick() {
    	//TODO connect to IP given, show game screen
    }

}
