package it.polimi.ingsw.FX;

import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {
    private List<Archipelago> listOfArchipelagos = new ArrayList<>();
    private List<Board> listOfBoards = new ArrayList<>();

    private Board myBoard;
    private List<ImageView> towers = new ArrayList<>();
    @FXML GridPane gridArchipelagos;

    //BoardElements
    @FXML Label nGreenDR;
    @FXML Label nRedDR;
    @FXML Label nYellowDR;
    @FXML Label nPinkDR;
    @FXML Label nBlueDR;
    @FXML ImageView tower1;
    @FXML ImageView tower2;
    @FXML ImageView tower3;
    @FXML ImageView tower4;
    @FXML ImageView tower5;
    @FXML ImageView tower6;
    @FXML ImageView tower7;
    @FXML ImageView tower8;

    @FXML ImageView profGreen;
    @FXML ImageView profRed;
    @FXML ImageView profYellow;
    @FXML ImageView profPink;
    @FXML ImageView profBlue;




    public void printArchipelagos() {
        Image archipelago = new Image(getClass().getResourceAsStream("/images/arch.jpg"));
        Image motherNature = new Image(getClass().getResourceAsStream("/images/mothernature.png"));
        int k = 0;

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 5; j++) {
                if(!(i == 1 && (j == 1 || j ==2 || j == 3)) && k < listOfArchipelagos.size() ){
                    ImageView arch = new ImageView(archipelago);
                    ImageView mn = new ImageView(motherNature);
                    arch.setFitHeight(100);
                    arch.setFitWidth(100);
                    mn.setFitHeight(20);
                    mn.setFitWidth(20);
                    gridArchipelagos.add(arch, j, i);
                    if(listOfArchipelagos.get(k).getIsMNPresent() == true) {
                        gridArchipelagos.add(mn, j, i);
                    }
                    k++;
                }
            }
        }
    }

    public void printMyBoard() {
        //Dining Room
        nGreenDR.setText(String.valueOf(myBoard.getDiningRoom().getStudentsByColor(StudsAndProfsColor.GREEN)));
        nRedDR.setText(String.valueOf(myBoard.getDiningRoom().getStudentsByColor(StudsAndProfsColor.RED)));
        nYellowDR.setText(String.valueOf(myBoard.getDiningRoom().getStudentsByColor(StudsAndProfsColor.YELLOW)));
        nPinkDR.setText(String.valueOf(myBoard.getDiningRoom().getStudentsByColor(StudsAndProfsColor.PINK)));
        nBlueDR.setText(String.valueOf(myBoard.getDiningRoom().getStudentsByColor(StudsAndProfsColor.BLUE)));

        //tower
        int tower = 0;
        for(ImageView i : towers) {
            if(tower < myBoard.getTowersOnBoard().getNumberOfTowers()) {
                i.setVisible(true);
            } else {
                i.setVisible(false);
            }
            tower++;
        }

        //professor

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image tower = new Image(getClass().getResourceAsStream("/images/tower.png"));
        printArchipelagos();
        printMyBoard();
        tower1 = new ImageView(tower);
        tower2 = new ImageView(tower);
        tower3 = new ImageView(tower);
        tower4 = new ImageView(tower);
        tower5 = new ImageView(tower);
        tower6 = new ImageView(tower);
        tower7 = new ImageView(tower);
        tower8 = new ImageView(tower);
        towers.add(tower1);
        towers.add(tower2);
        towers.add(tower3);
        towers.add(tower4);
        towers.add(tower5);
        towers.add(tower6);
        towers.add(tower7);
        towers.add(tower8);
    }
}
