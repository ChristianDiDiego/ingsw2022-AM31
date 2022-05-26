package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.utilities.constants.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardSceneController implements Initializable {
    Image tower = new Image(getClass().getResourceAsStream("/images/tower.png"));
    Image greenProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profgreen.png"));
    Image redProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profred.png"));
    Image yellowProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profyellow.png"));
    Image pinkProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profpink.png"));
    Image blueProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profblue.png"));
    Image[] professors = {greenProfessor,redProfessor,yellowProfessor,pinkProfessor,blueProfessor};
    Image greenStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentgreen.png"));
    Image redStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentred.png"));
    Image yellowStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentyellow.png"));
    Image pinkStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentpink.png"));
    Image blueStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentblue.png"));
    Image[] studentsImages = {greenStudent,redStudent,yellowStudent,pinkStudent,blueStudent};

    @FXML AnchorPane board1;
    @FXML AnchorPane board2;
    @FXML AnchorPane board3;
    @FXML AnchorPane board4;

    AnchorPane[] boards = {board1,board2,board3,board4};

    @FXML GridPane seb1;
    @FXML GridPane seb2;
    @FXML GridPane seb3;
    @FXML GridPane seb4;
    GridPane[] seb = {seb1,seb2,seb3,seb4};


    @FXML GridPane sdrb1;
    @FXML GridPane sdrb2;
    @FXML GridPane sdrb3;
    @FXML GridPane sdrb4;
    GridPane[] sdrb = {sdrb1,sdrb2,sdrb3,sdrb4};

    @FXML GridPane tb1;
    @FXML GridPane tb2;
    @FXML GridPane tb3;
    @FXML GridPane tb4;
    GridPane[] tb ={tb1,tb2,tb3,tb4};

    @FXML Label ob1;
    @FXML Label ob2;
    @FXML Label ob3;
    @FXML Label ob4;

    Label[] ob = {ob1,ob2,ob3,ob4};

    public void showAllBoards(List<Board> receivedBoards){

        switch (receivedBoards.size()){
            case 2:{
                board3.setVisible(false);
                board4.setVisible(false);
                ob3.setVisible(false);
                ob4.setVisible(false);
            }
            case 3:{
                board4.setVisible(false);
                ob4.setVisible(false);
            }
            case 4:
        }

        for(AnchorPane a : boards){
            a.getChildren().clear();
        }


        for(Board b : receivedBoards) {

            ob[receivedBoards.indexOf(b)].setText(b.getNickname());

            seb[receivedBoards.indexOf(b)].getChildren().clear();
            sdrb[receivedBoards.indexOf(b)].getChildren().clear();
            tb[receivedBoards.indexOf(b)].getChildren().clear();

            //tower

            int row = 0;
            int column = 0;
            for(int i = 0; i<b.getTowersOnBoard().getNumberOfTowers(); i++){
                tb[receivedBoards.indexOf(b)].add(new ImageView(tower), column,row);
                column++;
                if(column == 2){
                    column = 0;
                    row ++;
                }
            }

            //professor

            row=0;
            column = 0;
            for(int i = 0; i<Constants.NUMBEROFKINGDOMS; i++){
                if(b.getProfessorsTable().getHasProf(StudsAndProfsColor.values()[i])){
                  //pb[receivedBoards.indexOf(b)].add(new ImageView(professors[i], column,row);
                  row++;
                }
            }

            //students in dining room

            for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
                for (int j = 0; j < b.getDiningRoom().getStudentsByColor(StudsAndProfsColor.values()[i]); j++) {
                    ImageView st = new ImageView(studentsImages[i]);
                    st.setFitHeight(100);
                    st.setFitWidth(100);
                    sdrb[receivedBoards.indexOf(b)].add(st, j, i);
                }
            }

            //students in entrance
            System.out.println("I should print students in entrance");
            column = 0;
            row = 0;
            System.out.println("This board has: ");
            for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
                System.out.println(b.getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]) + " studs of " + i + "\n");
            }
            for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
                for (int j = 0; j < b.getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]); j++) {
                    if (row == 0 && column == 0) {
                        column++;
                    }
                    ImageView st = new ImageView(studentsImages[i]);
                    System.out.println("Image setted for kingdom " + i);
                    st.setFitWidth(120);
                    st.setFitHeight(120);
                    seb[receivedBoards.indexOf(b)].add(st, column, row);
                    System.out.println("Image added to grid");
                    column++;
                    if (column == 2) {
                        column = 0;
                        row++;
                    }
                }
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



    }
}
