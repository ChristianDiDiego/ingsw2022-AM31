package it.polimi.ingsw.FX;

import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.utilities.constants.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class MainSceneController implements Initializable {

    private List<ImageView> towers = new ArrayList<>();
    private List<ImageView> professors = new ArrayList<>();
    private List<GridPane> clouds = new ArrayList<>();

    FlowPane[] singleCellArchipelago;
    Image greenStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentgreen.png"));
    Image redStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentred.png"));
    Image yellowStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentyellow.png"));
    Image pinkStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentpink.png"));
    Image blueStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentblue.png"));
    Image[] studentsImages = {greenStudent,redStudent,yellowStudent,pinkStudent,blueStudent};

    @FXML GridPane gridArchipelagos;
    @FXML FlowPane arch0;
    @FXML FlowPane arch1;
    @FXML FlowPane arch2;
    @FXML FlowPane arch3;
    @FXML FlowPane arch4;
    @FXML FlowPane arch5;
    @FXML FlowPane arch6;
    @FXML FlowPane arch7;
    @FXML FlowPane arch8;
    @FXML FlowPane arch9;
    @FXML FlowPane arch10;
    @FXML FlowPane arch11;
    @FXML AnchorPane cloudPane;

    //BoardElements
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
    @FXML GridPane studentsInDR;
    @FXML GridPane studentsInEntrance;
    @FXML GridPane cloud1;
    @FXML GridPane cloud2;
    @FXML GridPane cloud3;
    @FXML GridPane cloud4;

    public void printArchipelagos(List<Archipelago> listOfArchipelagos) {
        for(FlowPane f : singleCellArchipelago) {
            f.getChildren().clear();
        }
        Image motherNature = new Image(getClass().getResourceAsStream("/images/mothernature.png"));
        Image studentRed = new Image(getClass().getResourceAsStream("/images/professors and students/studentred.png"));
        Image studentGreen = new Image(getClass().getResourceAsStream("/images/professors and students/studentgreen.png"));
        Image studentYellow = new Image(getClass().getResourceAsStream("/images/professors and students/studentyellow.png"));
        Image studentPink = new Image(getClass().getResourceAsStream("/images/professors and students/studentpink.png"));
        Image studentBlue = new Image(getClass().getResourceAsStream("/images/professors and students/studentblue.png"));
        Image[] studentColor = {studentRed, studentGreen, studentYellow, studentPink, studentBlue};
        int k = 0;
        for(int i = 0; i < listOfArchipelagos.size(); i++) {
            if(listOfArchipelagos.get(i).getIsMNPresent()) {
                ImageView mn = new ImageView(motherNature);
                mn.setFitHeight(50);
                mn.setFitWidth(50);
                singleCellArchipelago[i].getChildren().add(mn);
            }
            for(Island island : listOfArchipelagos.get(i).getBelongingIslands()) {
                for(int s = 0; s < Constants.NUMBEROFKINGDOMS; s++) {
                    for(int t = 0; t < island.getStudentsByColor(StudsAndProfsColor.values()[s]); t++) {
                        ImageView st = new ImageView(studentColor[s]);
                        st.setFitHeight(80);
                        st.setFitWidth(80);
                        singleCellArchipelago[i].getChildren().add(st);
                    }
                }
            }
            k++;
        }
        for(int i = k; i < Constants.NUMBEROFISLANDS; i++) {
            singleCellArchipelago[i].setVisible(false);
        }

    }

    public void printMyBoard(Board receivedBoard) {

        studentsInEntrance.getChildren().clear();
        studentsInDR.getChildren().clear();

        //tower
        int tower = 0;
        for(ImageView i : towers) {
            if(tower < receivedBoard.getTowersOnBoard().getNumberOfTowers()) {
                i.setVisible(true);
            } else {
                i.setVisible(false);
            }
            tower++;
        }

        //professor
        int prof = 0;
        for(ImageView i : professors){
            if(receivedBoard.getProfessorsTable().getHasProf(StudsAndProfsColor.values()[prof])){
                i.setVisible(true);
            }else{
                i.setVisible(false);
            }
            prof++;
        }

        //students in dining room


        for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
            for(int j = 0; j < receivedBoard.getDiningRoom().getStudentsByColor(StudsAndProfsColor.values()[i]); j++){
                ImageView st = new ImageView(studentsImages[i]);
                st.setFitHeight(100);
                st.setFitWidth(100);
                studentsInDR.add(st, j, i);
            }
        };

        //students in entrance
        System.out.println("I should print students in entrance");
        int column = 0;
        int row = 0;
        System.out.println("This board has: ");
        for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
            System.out.println(receivedBoard.getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]) + " studs of " + i + "\n");
        }
        for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
            for(int j = 0; j < receivedBoard.getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]); j++ ){
                if(row == 0 && column==0){
                    column++;
                }
                ImageView st =new ImageView(studentsImages[i]);
                System.out.println("Image setted for kingdom " + i);
                st.setFitWidth(120);
                st.setFitHeight(120);
                studentsInEntrance.add(st, column, row);
                System.out.println("Image added to grid");
                column++;
                if(column == 2){
                    column=0;
                    row++;
                }
            }
        }

    }

    public void printClouds(List<Cloud> cloudList){

        for(GridPane c : clouds){
            c.getChildren().clear();
        }

        switch (cloudList.size()){
            case 2:{
                cloud3.setVisible(false);
                cloud4.setVisible(false);
            }
            case 3:{
                cloud4.setVisible(false);
            }
            case 4:
        }

        int row=0;
        int column=0;
        for(Cloud c: cloudList){
            if (!c.getIsTaken()) {
                for (int j = 0; j < Constants.NUMBEROFKINGDOMS; j++) {
                    for (int k = 0; k < c.getStudents()[j]; k++) {
                        ImageView st = new ImageView();
                        st.setImage(studentsImages[j]);
                        st.setFitHeight(150);
                        st.setFitWidth(150);
                        clouds.get(cloudList.indexOf(c)).add(st, column, row);
                        column++;
                        if(column == 2){
                            column = 0;
                            row++;
                        }
                    }
                }
                column = 0;
                row = 0;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image tower = new Image(getClass().getResourceAsStream("/images/tower.png"));
        Image greenProfessor = new Image(getClass().getResourceAsStream("/images/professors and students/profgreen.png"));
        Image redProfessor = new Image(getClass().getResourceAsStream("/images/professors and students/profred.png"));
        Image yellowProfessor = new Image(getClass().getResourceAsStream("/images/professors and students/profyellow.png"));
        Image pinkProfessor = new Image(getClass().getResourceAsStream("/images/professors and students/profpink.png"));
        Image blueProfessor = new Image(getClass().getResourceAsStream("/images/professors and students/profblue.png"));

        singleCellArchipelago = new FlowPane[]{arch0, arch1, arch2, arch3, arch4, arch5, arch6, arch7, arch8, arch9, arch10, arch11};

        tower1.setImage(tower);
        tower2.setImage(tower);
        tower3.setImage(tower);
        tower4.setImage(tower);
        tower5.setImage(tower);
        tower6.setImage(tower);
        tower7.setImage(tower);
        tower8.setImage(tower);
        towers.add(tower1);
        towers.add(tower2);
        towers.add(tower3);
        towers.add(tower4);
        towers.add(tower5);
        towers.add(tower6);
        towers.add(tower7);
        towers.add(tower8);

        profGreen.setImage(greenProfessor);
        profRed = new ImageView(redProfessor);
        profYellow = new ImageView(yellowProfessor);
        profPink = new ImageView(pinkProfessor);
        profBlue = new ImageView(blueProfessor);

        professors.add(profGreen);
        professors.add(profRed);
        professors.add(profYellow);
        professors.add(profPink);
        professors.add(profBlue);


        clouds.add(cloud1);
        clouds.add(cloud2);
        clouds.add(cloud3);
        clouds.add(cloud4);

    }
}
