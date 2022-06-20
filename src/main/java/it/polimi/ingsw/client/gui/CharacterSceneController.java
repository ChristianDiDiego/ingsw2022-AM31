package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.utilities.Constants;
import it.polimi.ingsw.utilities.GameMessage;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;

/**
 * This class contains the controller to manage the character scene
 */
public class CharacterSceneController implements Initializable {
    Image character1 = new Image(getClass().getResourceAsStream("/images/expertMode/CarteTOT_front2.jpg"));
    Image character2 = new Image(getClass().getResourceAsStream("/images/expertMode/CarteTOT_front3.jpg"));
    Image character3 = new Image(getClass().getResourceAsStream("/images/expertMode/CarteTOT_front4.jpg"));
    Image character4 = new Image(getClass().getResourceAsStream("/images/expertMode/CarteTOT_front5.jpg"));
    Image character5 = new Image(getClass().getResourceAsStream("/images/expertMode/CarteTOT_front7.jpg"));
    Image character6 = new Image(getClass().getResourceAsStream("/images/expertMode/CarteTOT_front8.jpg"));
    Image character7 = new Image(getClass().getResourceAsStream("/images/expertMode/CarteTOT_front9.jpg"));
    Image character8 = new Image(getClass().getResourceAsStream("/images/expertMode/CarteTOT_front12.jpg"));

    List<Image> characters = new ArrayList<>();

    @FXML AnchorPane character1Pane;
    @FXML AnchorPane character2Pane;
    @FXML AnchorPane character3Pane;

    List<AnchorPane> charactersPane = new ArrayList<>();

    @FXML TextArea character1Label;
    @FXML TextArea character2Label;
    @FXML TextArea character3Label;

    @FXML ChoiceBox colorBox;
    @FXML ChoiceBox archipelagoBox;
    @FXML Label colorLabel;
    @FXML Label archipelagoLabel;
    @FXML ChoiceBox firstEnBox;
    @FXML ChoiceBox secondEnBox;
    @FXML ChoiceBox firstDRBox;
    @FXML ChoiceBox secondDRBox;
    List<ChoiceBox> switchBox = new ArrayList<>();
    @FXML Label firstEnLabel;
    @FXML Label secondEnLabel;
    @FXML Label firstDRLabel;
    @FXML Label secondDRLabel;
    List<Label> switchLabel = new ArrayList<>();
    List<TextArea> charactersLabel = new ArrayList<>();

    @FXML TextArea messageArea;
    private int wallet = 0;
    List<String> charactersId = new ArrayList<>();
    List<Integer> charactersPrice = new ArrayList<>();
    private boolean firtsCharacterOfTheTurn = true;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Add a listener to this scene
     *
     * @param pcl
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Shows the characters of the game if the mode is expert
     *
     * @param id
     * @param description
     * @param price
     */
    public void printCharacters(List<String> id, List<String> description, List<Integer> price){
        charactersId.clear();
        charactersPrice.clear();
        firtsCharacterOfTheTurn = true;
        messageArea.setText(GameMessage.characterInstructions);
        for(int i = 0; i < id.size(); i++){
            int idCharacter = Integer.parseInt(id.get(i));
            if(idCharacter == 6) {
                colorBox.setVisible(true);
                colorLabel.setVisible(true);
            } else if(idCharacter == 1 || idCharacter == 3) {
                archipelagoBox.setVisible(true);
                archipelagoLabel.setVisible(true);
            } else if(idCharacter == 7) {
                for(int j = 0; j < switchBox.size(); j++) {
                    switchBox.get(j).setVisible(true);
                    switchBox.get(j).getSelectionModel().select(0);
                    switchLabel.get(j).setVisible(true);
                }
            }

            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
            BackgroundImage backgroundImage = new BackgroundImage(characters.get(idCharacter - 1), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
            Background background = new Background(backgroundImage);
            charactersPane.get(i).setBackground(background);
            charactersPane.get(i).setOpacity(1.0);

            charactersLabel.get(i).setText(description.get(i));

            charactersPane.get(i).setAccessibleText("" + id.get(i));
            setOnClickCharacter(charactersPane.get(i));

            charactersId.add(id.get(i));
            charactersPrice.add(price.get(i));
        }
    }

    /**
     * Add the possibility to click on the characters' image to play them
     *
     * @param character
     */
    private void setOnClickCharacter(AnchorPane character){
        character.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int characterNumber = -1;
                for(int i = 0; i < Constants.NUMBEROFPLAYABLECHARACTERS; i++) {
                    if(charactersId.get(i).equals(character.getAccessibleText())) {
                        characterNumber = i;
                    }
                }
                if(firtsCharacterOfTheTurn) {
                    if(wallet >= charactersPrice.get(characterNumber)) {
                        if(character.getAccessibleText().equals("6")) {
                            playCharacter6(character.getAccessibleText());
                        } else if(character.getAccessibleText().equals("1") || character.getAccessibleText().equals("3")){
                            playCharacter1or3(character.getAccessibleText());
                        } else if(character.getAccessibleText().equals("7")) {
                            playCharacter7(character.getAccessibleText());
                        }else {
                            playSimpleCharacter(character.getAccessibleText());
                        }
                        character.setOpacity(0.5);
                        firtsCharacterOfTheTurn = false;
                        messageArea.setText("You played the character");
                        event.consume();
                    } else {
                        messageArea.setText("You don't have enough coins");
                        event.consume();
                    }
                }
            }
        });
    }

    /**
     * Shows the menu to select an archipelago in one of the characters needs it
     *
     * @param listReceived
     */
    public void setArchipelagos(List<Archipelago> listReceived) {
        archipelagoBox.getItems().clear();
        for(int i = 0; i < listReceived.size(); i++) {
            String id = String.valueOf(listReceived.get(i).getIdArchipelago());
            archipelagoBox.getItems().add(id);
        }
        archipelagoBox.getSelectionModel().select(0);
    }

    /**
     * Sets the wallet to see if the player has enough money to play the character
     *
     * @param coins
     */
    public void setWallet(int coins) {
        wallet = coins;
    }

    /**
     * Plays a character who doesn't need further specifications
     *
     * @param characterSelected
     */
    private void playSimpleCharacter(String characterSelected){
        String playSelectedCloud = "CHARACTER " + characterSelected;
        support.firePropertyChange("characterPlayed", "", playSelectedCloud);
    }

    /**
     * Plays the character 1 or 3 that need an archipelago on which to use the power
     *
     * @param characterSelected
     */
    private void playCharacter1or3(String characterSelected) {
        if(archipelagoBox.getValue() != null) {
            String playSelectedCloud = "CHARACTER " + characterSelected + " " + archipelagoBox.getValue().toString();
            support.firePropertyChange("characterPlayed", "", playSelectedCloud);
        }
    }

    /**
     * Plays the character 6 who need a color to use the power
     *
     * @param characterSelected
     */
    private void playCharacter6(String characterSelected) {
        if (colorBox.getValue() != null) {
            String playSelectedCloud = "CHARACTER " + characterSelected + " " + colorBox.getValue().toString();
            support.firePropertyChange("characterPlayed", "", playSelectedCloud);
        }
    }

    /**
     * Plays the character 7 who need four student for the swap
     *
     * @param characterSelected
     */
    private void playCharacter7(String characterSelected) {
        if (colorBox.getValue() != null) {
            String playSelectedCloud = "CHARACTER " + characterSelected + " " + switchBox.get(0).getValue().toString() + "," + switchBox.get(1).getValue().toString() + "," + switchBox.get(2).getValue().toString() + "," + switchBox.get(3).getValue().toString();
            support.firePropertyChange("characterPlayed", "", playSelectedCloud);
        }
    }

    /**
     * Override of the method initialize to set all the components of the scene
     * according to the game in progress
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        characters.add(character1);
        characters.add(character2);
        characters.add(character3);
        characters.add(character4);
        characters.add(character5);
        characters.add(character6);
        characters.add(character7);
        characters.add(character8);

        charactersPane.add(character1Pane);
        charactersPane.add(character2Pane);
        charactersPane.add(character3Pane);

        character1Label.setWrapText(true);
        character2Label.setWrapText(true);
        character3Label.setWrapText(true);

        charactersLabel.add(character1Label);
        charactersLabel.add(character2Label);
        charactersLabel.add(character3Label);

        colorBox.getItems().add("GREEN");
        colorBox.getItems().add("RED");
        colorBox.getItems().add("YELLOW");
        colorBox.getItems().add("PINK");
        colorBox.getItems().add("BLUE");

        colorBox.getSelectionModel().select(0);

        colorBox.setVisible(false);
        colorLabel.setVisible(false);
        archipelagoBox.setVisible(false);
        archipelagoLabel.setVisible(false);

        switchBox.add(firstEnBox);
        switchBox.add(secondEnBox);
        switchBox.add(firstDRBox);
        switchBox.add(secondDRBox);

        switchLabel.add(firstEnLabel);
        switchLabel.add(secondEnLabel);
        switchLabel.add(firstDRLabel);
        switchLabel.add(secondDRLabel);

        for(int i = 0; i < switchBox.size(); i++) {
            switchBox.get(i).setVisible(false);
            switchLabel.get(i).setVisible(false);

            switchBox.get(i).getItems().add("GREEN");
            switchBox.get(i).getItems().add("RED");
            switchBox.get(i).getItems().add("YELLOW");
            switchBox.get(i).getItems().add("PINK");
            switchBox.get(i).getItems().add("BLUE");
        }
    }
}
