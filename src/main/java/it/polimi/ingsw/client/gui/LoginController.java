package it.polimi.ingsw.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    Label usernameText;
    @FXML
    Label textHowManyPlayers;
    @FXML
    Label textMode;
    @FXML
    Label textColor;
    @FXML
    TextField usernameTextField;
    @FXML
    Button submitUsername;
    @FXML
    Button submitNumberOfPlayers;
    @FXML
    Button submitMode;
    @FXML
    Button submitColor;
    @FXML
    Label waitingForPlayers;

    boolean notFirstPlayer = false;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    @FXML
    RadioButton radio2,radio3,radio4,radiostd,radioexp,radioWhite,radioBlack,radioGrey;
    String username ;

    @FXML
    Label errorMessagesLabel;
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void submitUsername(ActionEvent event) {


        try {
            errorMessagesLabel.setText("");
            username = usernameTextField.getText();
            usernameText.setText("Welcome " + username);
            support.firePropertyChange("username", "", username );
            submitUsername.setVisible(false);
            usernameTextField.setVisible(false);

            if(!notFirstPlayer){
                textHowManyPlayers.setVisible(true);
                radio2.setVisible(true);
                radio3.setVisible(true);
                radio4.setVisible(true);
                submitNumberOfPlayers.setVisible(true);
            }else{
                textColor.setVisible(true);
                radioWhite.setVisible(true);
                radioBlack.setVisible(true);
                radioGrey.setVisible(true);
                submitColor.setVisible(true);
            }


        }
        catch (Exception e) {
            usernameText.setText("error");
        }
    }

    public void submitNumberOfPlayers(ActionEvent event){
        String numberOfPlayers = null;
        if(radio2.isSelected()) {
            System.out.println(radio2.getText());
            numberOfPlayers = radio2.getText();
        }
        else if(radio3.isSelected()) {
            System.out.println(radio3.getText());
            numberOfPlayers = radio3.getText();
        }
        else if(radio4.isSelected()) {
            System.out.println(radio4.getText());
            numberOfPlayers = radio4.getText();
        }

        support.firePropertyChange("numberOfPlayers", "",  numberOfPlayers );
        submitNumberOfPlayers.setVisible(false);
        radio2.setDisable(true);
        radio3.setDisable(true);
        radio4.setDisable(true);

        textMode.setVisible(true);
        radiostd.setVisible(true);
        radioexp.setVisible(true);
        submitMode.setVisible(true);
    }

    public void submitMode(ActionEvent event){
        String mode = null;
        if(radiostd.isSelected()) {
            System.out.println(radiostd.getText());
            mode = "0";
        } else if(radioexp.isSelected()) {
            System.out.println(radioexp.getText());
            mode = "1";
        }

        support.firePropertyChange("mode", "",  mode );
        radiostd.setDisable(true);
        radioexp.setDisable(true);
        submitMode.setVisible(false);

        textColor.setVisible(true);
        radioWhite.setVisible(true);
        radioBlack.setVisible(true);
        radioGrey.setVisible(true);
        submitColor.setVisible(true);
    }

    public void submitColor(ActionEvent event){
        String color = null;
        if(radioWhite.isSelected()) {
            System.out.println(radioWhite.getText());
            color = "0";
        } else if(radioBlack.isSelected()) {
            System.out.println(radioBlack.getText());
            color = "1";
        } else if(radioGrey.isSelected()) {
            System.out.println(radioGrey.getText());
            color = "2";
        }
        support.firePropertyChange("color", "",  color);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameText.setText("There is another player in setup, please wait...");
        submitUsername.setVisible(false);
        usernameTextField.setVisible(false);
        textHowManyPlayers.setVisible(false);
        textMode.setVisible(false);
        textColor.setVisible(false);
        radio2.setVisible(false);
        radio3.setVisible(false);
        radio4.setVisible(false);
        radiostd.setVisible(false);
        radioexp.setVisible(false);
        radioWhite.setVisible(false);
        radioBlack.setVisible(false);
        radioGrey.setVisible(false);
        submitNumberOfPlayers.setVisible(false);
        submitMode.setVisible(false);
        submitColor.setVisible(false);
        waitingForPlayers.setVisible(false);
    }

    public MainSceneController switchToMainScene() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mainScene.fxml"));
        Parent root = loader.load();
        usernameText.getScene().setRoot(root);
        return loader.getController();
    }
    public void setWaitingForOtherPlayers(){
        waitingForPlayers.setVisible(true);
    }

    public void setNotFirstPlayer(){
        notFirstPlayer = true;
    }

    public void allowSetup(){
        usernameText.setText("Welcome!");
        submitUsername.setVisible(true);
        usernameTextField.setVisible(true);

    }
    public void usernameAlreadyUsed(String message){
        errorMessagesLabel.setText(message);
        submitUsername.setVisible(true);
        usernameTextField.setVisible(true);

        textHowManyPlayers.setVisible(false);
        radio2.setVisible(false);
        radio3.setVisible(false);
        radio4.setVisible(false);
        submitNumberOfPlayers.setVisible(false);

        textColor.setVisible(false);
        radioWhite.setVisible(false);
        radioBlack.setVisible(false);
        radioGrey.setVisible(false);
        submitColor.setVisible(false);
    }

    public void colorAlreadyUsed(String message){
        errorMessagesLabel.setText(message);
    }
}

