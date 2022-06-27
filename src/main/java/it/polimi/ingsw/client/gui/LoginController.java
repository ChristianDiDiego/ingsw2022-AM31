package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.utilities.EventName;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class contains the controller to manage the login scene
 */
public class LoginController implements Initializable {
    @FXML
    Label welcomeText;
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

    int numberOfPlayers;

    boolean notFirstPlayer = false;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    @FXML
    RadioButton radio2, radio3, radio4, radiostd, radioexp, radioWhite, radioBlack, radioGrey;
    String username;
    @FXML
    Label errorMessagesLabel;

    private final FXMLLoader mainSceneLoader = new FXMLLoader(getClass().getClassLoader().getResource("mainScene.fxml"));
    Parent mainRoot;
    Scene mainScene;
    private Stage mainStage;


    /**
     * Add a listener to this scene
     *
     * @param pcl listener
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Sends to server the nickname chosen by the player
     */
    public void submitUsername() {
        try {
            if (!Objects.equals(usernameTextField.getText(), "")) {
                errorMessagesLabel.setText("");
                username = usernameTextField.getText();
                welcomeText.setText("Welcome " + username);
                support.firePropertyChange(EventName.InsertedUsername, "", username);
                submitUsername.setVisible(false);
                usernameTextField.setVisible(false);
                usernameText.setVisible(false);

            }
        } catch (Exception e) {
            welcomeText.setText("error");
        }
    }

    public void showHowManyPlayers(){
        textHowManyPlayers.setVisible(true);
        radio2.setVisible(true);
        radio3.setVisible(true);
        radio4.setVisible(true);
        submitNumberOfPlayers.setVisible(true);
    }

    /**
     * Sends to server the number of players the current player wants to play with
     */
    public void submitNumberOfPlayers() {
        String numberOfPlayers = null;
        if (radio2.isSelected()) {
            System.out.println(radio2.getText());
            numberOfPlayers = radio2.getText();
        } else if (radio3.isSelected()) {
            System.out.println(radio3.getText());
            numberOfPlayers = radio3.getText();
        } else if (radio4.isSelected()) {
            System.out.println(radio4.getText());
            numberOfPlayers = radio4.getText();
        }

        support.firePropertyChange("numberOfPlayers", "", numberOfPlayers);
        if (numberOfPlayers != null) {
            this.numberOfPlayers = Integer.parseInt(numberOfPlayers);
        }
        submitNumberOfPlayers.setVisible(false);
        radio2.setDisable(true);
        radio3.setDisable(true);
        radio4.setDisable(true);

        textMode.setVisible(true);
        radiostd.setVisible(true);
        radioexp.setVisible(true);
        submitMode.setVisible(true);
    }

    /**
     * Sends the mode in which the current player wants to play
     */
    public void submitMode() {
        String mode = null;
        if (radiostd.isSelected()) {
            System.out.println(radiostd.getText());
            mode = "0";
        } else if (radioexp.isSelected()) {
            System.out.println(radioexp.getText());
            mode = "1";
        }

        support.firePropertyChange("mode", "", mode);
        radiostd.setDisable(true);
        radioexp.setDisable(true);
        submitMode.setVisible(false);


    }

    public void showColorChoice(boolean showGrey){
        textColor.setVisible(true);
        radioWhite.setVisible(true);
        radioBlack.setVisible(true);
        if (showGrey) {
            radioGrey.setVisible(true);
        }
        submitColor.setVisible(true);
    }
    /**
     * Sends the color of tower the current player wants to play with
     */
    public void submitColor() {
        String color = null;
        if (radioWhite.isSelected()) {
            System.out.println(radioWhite.getText());
            color = "0";
        } else if (radioBlack.isSelected()) {
            System.out.println(radioBlack.getText());
            color = "1";
        } else if (radioGrey.isSelected()) {
            System.out.println(radioGrey.getText());
            color = "2";
        }
        support.firePropertyChange("color", "", color);
    }

    /**
     * Override of the method initialize to set all the components of the scene
     * according to the game in progress
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            mainRoot = mainSceneLoader.load();
            mainStage = new Stage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        welcomeText.setText("There is another player in setup, please wait...");
        usernameText.setVisible(false);
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

    /**
     * Switch from login scene to the game's scene
     *
     * @return controller of the mainScene
     * @throws IOException if exception
     */
    public MainSceneController getMainSceneController() throws IOException {
        return mainSceneLoader.getController();
    }

    public Stage getMainStage(){
        return mainStage;
    }

    public void switchToMainScene() {
        mainScene = new Scene(mainRoot);
        mainStage.setScene(mainScene);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/eriantys_logo.jpg")));
        mainStage.getIcons().add(icon);
        mainStage.setTitle("Eriantys | " + username);
        mainStage.setMaximized(true);
        mainStage.show();

        Stage stage = (Stage) welcomeText.getScene().getWindow();
        stage.close();
    }

    /**
     * If this player isn't the last one, shows the sign which invites
     * him to wait for other players
     */
    public void setWaitingForOtherPlayers(String waitingPlayers) {
        waitingForPlayers.setVisible(true);
        waitingForPlayers.setText(waitingPlayers);
        textColor.setVisible(false);
        radioWhite.setVisible(false);
        radioBlack.setVisible(false);
        radioGrey.setVisible(false);
        submitColor.setVisible(false);
    }

    /**
     * Set true notFirstPlayer if this player isn't the first of the match
     */
    public void setNotFirstPlayer() {
        notFirstPlayer = true;
    }

    /**
     * Set the initial window that will be displayed by the player
     */
    public void allowSetup() {
        welcomeText.setText("Welcome!");
        usernameText.setVisible(true);
        submitUsername.setVisible(true);
        usernameTextField.setVisible(true);
    }

    /**
     * Show a message to the player if the nickname chosen is already used
     *
     * @param message error message to be displayed
     */
    public void usernameAlreadyUsed(String message) {
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

    /**
     * Show a message to the player if the color chosen is already used
     *
     * @param message to be showed
     */
    public void printError(String message) {
        errorMessagesLabel.setText(message);
    }

}

