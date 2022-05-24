package it.polimi.ingsw.FX;

import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.utilities.constants.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

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
    @FXML GridPane gridArchipelagos;
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

    /*
    @FXML HBox gSDR;
    @FXML HBox rSDR;
    @FXML HBox ySDR;
    @FXML HBox pSDR;
    @FXML HBox bSDR;
     */

    @FXML GridPane studentsInDR;
    @FXML GridPane studentsInEntrance;
    @FXML GridPane cloud1;
    @FXML GridPane cloud2;
    @FXML GridPane cloud3;
    @FXML GridPane cloud4;




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
        Image greenStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentgreen.png"));
        Image redStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentred.png"));
        Image yellowStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentyellow.png"));
        Image pinkStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentpink.png"));
        Image blueStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentblue.png"));

        Image[] studentsImages = {greenStudent,redStudent,yellowStudent,pinkStudent,blueStudent};

        studentsInDR = new GridPane();

        for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
            for(int j = 0; j < myBoard.getDiningRoom().getStudentsByColor(StudsAndProfsColor.values()[i]); j++){
                studentsInDR.add(new ImageView(studentsImages[i]), i, j);
            }
        };

       /* gSDR = new HBox(10);
        rSDR = new HBox(10);
        ySDR = new HBox(10);
        pSDR = new HBox(10);
        bSDR = new HBox(10);

        studentsInDiningRoom.add(gSDR);
        studentsInDiningRoom.add(rSDR);
        studentsInDiningRoom.add(ySDR);
        studentsInDiningRoom.add(pSDR);
        studentsInDiningRoom.add(bSDR);


        for(HBox i : studentsInDiningRoom){
           for(int stud =0 ; stud < myBoard.getDiningRoom().getStudentsByColor(StudsAndProfsColor.values()[studentsInDiningRoom.indexOf(i)]); stud ++){
               i.getChildren().add(new ImageView(studentsImages[studentsInDiningRoom.indexOf(i)]));
            }
        }

        */


        //students in entrance
        int column = 0;
        int row = 0;
        studentsInEntrance = new GridPane();
        for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
            for(int j = 0; j < myBoard.getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]); j++ ){
                if(row == 0 && column==0){
                    column++;
                }
                studentsInEntrance.add(new ImageView(studentsImages[i]), row, column);
                column++;
                if(column == 2){
                    column=0;
                    row++;
                }
            }
        }

    }

    public void printClouds(){
        Image greenStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentgreen.png"));
        Image redStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentred.png"));
        Image yellowStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentyellow.png"));
        Image pinkStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentpink.png"));
        Image blueStudent = new Image(getClass().getResourceAsStream("/images/professors and students/studentblue.png"));

        int row=0;
        int column=0;
        for(Cloud c: cloudList){
            if (!c.getIsTaken()) {
                for (int j = 0; j < Constants.NUMBEROFKINGDOMS; j++) {
                    for (int k = 0; k < c.getStudents()[j]; k++) {
                        switch (StudsAndProfsColor.values()[j]) {
                            case GREEN ->clouds.get(cloudList.indexOf(c)).add(new ImageView(greenStudent),row,column);
                            case RED ->clouds.get(cloudList.indexOf(c)).add(new ImageView(redStudent),row,column);
                            case YELLOW ->clouds.get(cloudList.indexOf(c)).add(new ImageView(yellowStudent),row,column);
                            case PINK ->clouds.get(cloudList.indexOf(c)).add(new ImageView(pinkStudent),row,column);
                            case BLUE ->clouds.get(cloudList.indexOf(c)).add(new ImageView(blueStudent),row,column);

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

        Archipelago a = new Archipelago(2);
        Archipelago b = new Archipelago(3);
        b.changeMNPresence();
        listOfArchipelagos.add(a);
        listOfArchipelagos.add(b);

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

        profGreen = new ImageView(greenProfessor);
        profRed = new ImageView(redProfessor);
        profYellow = new ImageView(yellowProfessor);
        profPink = new ImageView(pinkProfessor);
        profBlue = new ImageView(blueProfessor);

        professors.add(profGreen);
        professors.add(profRed);
        professors.add(profYellow);
        professors.add(profPink);
        professors.add(profBlue);

        cloud1 = new GridPane();
        cloud2 = new GridPane();
        cloud3 = new GridPane();
        cloud4 = new GridPane();

        clouds.add(cloud1);
        clouds.add(cloud2);
        clouds.add(cloud3);
        clouds.add(cloud4);


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


        printArchipelagos();
      //  printMyBoard();
        //printClouds();
    }
}
