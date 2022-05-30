package it.polimi.ingsw.client.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;

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

    @FXML Label character1Label;
    @FXML Label character2Label;
    @FXML Label character3Label;

    List<Label> charactersLabel = new ArrayList<>();

    public void printCharacters(List<Integer> id,List<String> description){
        for(int i = 0; i <id.size(); i++){
            charactersPane.get(i).setStyle("-fx-background-image: url('" + characters.get(id.get(i)) + "'); " + "-fx-background-size: stretch;");
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

        charactersLabel.add(character1Label);
        charactersLabel.add(character2Label);
        charactersLabel.add(character3Label);
    }
}
