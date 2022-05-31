package it.polimi.ingsw.client.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    List<TextArea> charactersLabel = new ArrayList<>();

    public void printCharacters(List<String> id,List<String> description){
        for(int i = 0; i < id.size(); i++){
            int idCharacter = Integer.parseInt(id.get(i));

            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
            BackgroundImage backgroundImage = new BackgroundImage(characters.get(idCharacter - 1), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
            Background background = new Background(backgroundImage);
            charactersPane.get(i).setBackground(background);

            //charactersPane.get(i).setStyle("-fx-background-image: url('" + characters.get(idCharacter - 1) + "'); " + "-fx-background-size: stretch;");
            charactersLabel.get(i).setText(description.get(i));
        }
    }


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
    }
}
