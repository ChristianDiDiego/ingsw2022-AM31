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
    private List<Archipelago> listOfArchipelagos = new ArrayList<>();
    private List<Board> listOfBoards = new ArrayList<>();
    private Board myBoard;
    private List<Cloud> cloudList = new ArrayList<>();
    private List<ImageView> towers = new ArrayList<>();
    private List<HBox> studentsInDiningRoom = new ArrayList<>();
    private List<ImageView> professors = new ArrayList<>();
    private List<GridPane> clouds = new ArrayList<>();

    FlowPane[] singleCellArchipelago;
    Image greenStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentgreen.png"));
    Image redStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentred.png"));
    Image yellowStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentyellow.png"));
    Image pinkStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentpink.png"));
    Image blueStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentblue.png"));
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

    /*public void printArchipelagos() {
        Image archipelago = new Image(getClass().getResourceAsStream("/images/arch.jpg"));
        Image motherNature = new Image(getClass().getResourceAsStream("/images/mothernature.png"));
        Image studentRed = new Image(getClass().getResourceAsStream("/images/professors and students/studentred.png"));
        Image studentGreen = new Image(getClass().getResourceAsStream("/images/professors and students/studentgreen.png"));
        Image studentYellow = new Image(getClass().getResourceAsStream("/images/professors and students/studentyellow.png"));
        Image studentPink = new Image(getClass().getResourceAsStream("/images/professors and students/studentpink.png"));
        Image studentBlue = new Image(getClass().getResourceAsStream("/images/professors and students/studentblue.png"));
        Image[] studentColor = {studentGreen, studentRed, studentYellow, studentPink, studentBlue};
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
                    for(Island island : listOfArchipelagos.get(k).getBelongingIslands()) {
                        for(int s = 0; s < Constants.NUMBEROFKINGDOMS; s++) {
                            for(int t = 0; t < island.getStudentsByColor(StudsAndProfsColor.values()[s]); t++) {
                                ImageView st = new ImageView(studentColor[s]);
                                st.setFitHeight(80);
                                st.setFitWidth(80);
                                gridArchipelagos.add(st, j, i);
                            }
                        }
                    }
                    k++;
                }
            }
        }
    }*/

    public void printArchipelagos() {
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

    public void printMyBoard() {
        myBoard = new Board("ca");
        int[] toAdd = {1,2,3,1,0};
        //studentsInDR.setGridLinesVisible(true);
        //studentsInEntrance.setGridLinesVisible(true);

        myBoard.getEntrance().addStudent(toAdd);
        myBoard.getDiningRoom().addStudent(StudsAndProfsColor.BLUE);
        myBoard.getDiningRoom().addStudent(StudsAndProfsColor.BLUE);
        myBoard.getDiningRoom().addStudent(StudsAndProfsColor.BLUE);
        myBoard.getDiningRoom().addStudent(StudsAndProfsColor.RED);
        myBoard.getDiningRoom().addStudent(StudsAndProfsColor.YELLOW);
        myBoard.getDiningRoom().addStudent(StudsAndProfsColor.YELLOW);
        myBoard.getDiningRoom().addStudent(StudsAndProfsColor.GREEN);

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
        int prof = 0;
        for(ImageView i : professors){
            if(myBoard.getProfessorsTable().getHasProf(StudsAndProfsColor.values()[prof])){
                i.setVisible(true);
            }else{
                i.setVisible(false);
            }
            prof++;
        }

        //students in dining room

        Image[] studentsImages = {greenStudent,redStudent,yellowStudent,pinkStudent,blueStudent};

        for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
            for(int j = 0; j < myBoard.getDiningRoom().getStudentsByColor(StudsAndProfsColor.values()[i]); j++){
                ImageView st = new ImageView(studentsImages[i]);
                st.setFitHeight(100);
                st.setFitWidth(100);
                studentsInDR.add(st, j, i);
            }
        };

        //students in entrance
        int column = 0;
        int row = 0;
        for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
            for(int j = 0; j < myBoard.getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]); j++ ){
                if(row == 0 && column==0){
                    column++;
                }
                ImageView st =new ImageView(studentsImages[i]);
                st.setFitWidth(120);
                st.setFitHeight(120);
                studentsInEntrance.add(st, column, row);
                column++;
                if(column == 2){
                    column=0;
                    row++;
                }
            }
        }

    }

    public void printClouds(){
        int row=0;
        int column=0;
        for(Cloud c: cloudList){
            if (!c.getIsTaken()) {
                for (int j = 0; j < Constants.NUMBEROFKINGDOMS; j++) {
                    for (int k = 0; k < c.getStudents()[j]; k++) {
                        switch (StudsAndProfsColor.values()[j]) {
                            case GREEN -> {
                                ImageView st = new ImageView(greenStudent);
                                st.setFitHeight(150);
                                st.setFitWidth(150);
                                clouds.get(cloudList.indexOf(c)).add(st, column, row);
                            }
                            case RED ->{
                                ImageView st = new ImageView(redStudent);
                                st.setFitHeight(150);
                                st.setFitWidth(150);
                                clouds.get(cloudList.indexOf(c)).add(st, column, row);
                            }
                            case YELLOW ->{
                                ImageView st = new ImageView(yellowStudent);
                                st.setFitHeight(150);
                                st.setFitWidth(150);
                                clouds.get(cloudList.indexOf(c)).add(st, column, row);
                            }
                            case PINK ->{
                                ImageView st = new ImageView(pinkStudent);
                                st.setFitHeight(150);
                                st.setFitWidth(150);
                                clouds.get(cloudList.indexOf(c)).add(st, column, row);
                            }
                            case BLUE ->{
                                ImageView st = new ImageView(blueStudent);
                                st.setFitHeight(150);
                                st.setFitWidth(150);
                                clouds.get(cloudList.indexOf(c)).add(st, column, row);
                            }

                        }
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

        Cloud cloud = new Cloud(1);
        Cloud cloud2 = new Cloud(2);
        cloudList.add(cloud);
        cloudList.add(cloud2);
        int[] toAdd = {1,2, 1,0,0};
        cloud.addStudents(toAdd);
        toAdd = new int[]{0, 1, 1, 2, 0};
        cloud2.addStudents(toAdd);


        /*
        switch (numberOFPlayers){
            case 2:{
                cloud3.setVisible(false);
                cloud4.setVisible(false);
            }
            case 3:{
                cloud4.setVisible(false);
            }
            case 4:
        }
         */

        //printArchipelagos();
        //printMyBoard();
        //printClouds();
    }
}
