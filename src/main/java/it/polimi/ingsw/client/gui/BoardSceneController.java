package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.utilities.constants.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardSceneController implements Initializable {
    Image blackTower = new Image(getClass().getResourceAsStream("/images/blacktower.png"));
    Image whiteTower = new Image(getClass().getResourceAsStream("/images/whitetower.png"));
    Image greyTower = new Image(getClass().getResourceAsStream("/images/greytower.png"));

    Image greenProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profgreen.png"));
    Image redProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profred.png"));
    Image yellowProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profyellow.png"));
    Image pinkProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profpink.png"));
    Image blueProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profblue.png"));
    List<Image> professors = new ArrayList<>();
    Image greenStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentgreen.png"));
    Image redStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentred.png"));
    Image yellowStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentyellow.png"));
    Image pinkStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentpink.png"));
    Image blueStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentblue.png"));
    List<Image> students = new ArrayList<>();
    @FXML AnchorPane board1;
    @FXML AnchorPane board2;
    @FXML AnchorPane board3;
    @FXML AnchorPane board4;
    List<AnchorPane> boards = new ArrayList<>();

    @FXML GridPane seb1;
    @FXML GridPane seb2;
    @FXML GridPane seb3;
    @FXML GridPane seb4;
    List<GridPane> seb = new ArrayList<>();


    @FXML GridPane sdrb1;
    @FXML GridPane sdrb2;
    @FXML GridPane sdrb3;
    @FXML GridPane sdrb4;
    List<GridPane> sdrb = new ArrayList<>();

    @FXML GridPane tb1;
    @FXML GridPane tb2;
    @FXML GridPane tb3;
    @FXML GridPane tb4;

    List<GridPane> tb = new ArrayList<>();

    @FXML Label ob1;
    @FXML Label ob2;
    @FXML Label ob3;
    @FXML Label ob4;

    List<Label> ob = new ArrayList<>();

    @FXML GridPane pb1;
    @FXML GridPane pb2;
    @FXML GridPane pb3;
    @FXML GridPane pb4;

    List<GridPane> pb = new ArrayList<>();

    //Font font = Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 18);
    Font font = Font.font("Calisto MT", FontPosture.ITALIC, 18);

    private List<Board> receivedBoards = new ArrayList<>();

    public void setReceivedBoards(List<Board> boards) {
        this.receivedBoards = boards;
    }

    public void printLastUsedCard(List<Player> players) {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).getLastUsedCard() != null) {
                String nickname = ob.get(i).getText();
                String lastUsedCard = ", last used card: " + players.get(i).getLastUsedCard().getPower() + "   " + players.get(i).getLastUsedCard().getMaxSteps();
                ob.get(i).setText(nickname + lastUsedCard);
                ob.get(i).setFont(font);
            }
        }
    }

    public void showAllBoards(){

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



        for(int j = 0; j < receivedBoards.size(); j++) {

            ob.get(j).setText("Player: " + receivedBoards.get(j).getNickname());
            ob.get(j).setFont(font);

            seb.get(j).getChildren().clear();
            sdrb.get(j).getChildren().clear();
            tb.get(j).getChildren().clear();
            pb.get(j).getChildren().clear();

            //tower

            int row = 0;
            int column = 0;
            for(int i = 0; i < receivedBoards.get(j).getTowersOnBoard().getNumberOfTowers(); i++){
                ImageView t;
                switch (receivedBoards.get(j).getColorOfTower()){
                    case WHITE -> t = new ImageView(whiteTower);
                    case GREY -> t = new ImageView(greyTower);
                    default -> t = new ImageView(blackTower);

                }
                t.setFitHeight(40);
                t.setFitWidth(40);
                tb.get(j).add(t, column,row);
                GridPane.setHalignment(t, HPos.CENTER);
                column++;
                if(column == 2){
                    column = 0;
                    row ++;
                }
            }


            //students in entrance
            System.out.println("I should print students in entrance");
            column = 0;
            row = 0;
            System.out.println("This board has: ");
            for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
                System.out.println(receivedBoards.get(j).getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]) + " studs of " + i + "\n");
            }
            for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
                for (int k = 0; k < receivedBoards.get(j).getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]); k++) {
                    if (row == 0 && column == 0) {
                        column++;
                    }
                    ImageView st = new ImageView(students.get(i));
                    st.setFitWidth(80);
                    st.setFitHeight(80);
                    System.out.println("Image setted for kingdom " + i);

                    seb.get(j).add(st, column, row);
                    GridPane.setHalignment(st, HPos.CENTER);
                    System.out.println("Image added to grid");
                    column++;
                    if (column == 2) {
                        column = 0;
                        row++;
                    }
                }
            }

            //professor
            row=0;
            column = 0;
            for(int i = 0; i<Constants.NUMBEROFKINGDOMS; i++){
                if(receivedBoards.get(j).getProfessorsTable().getHasProf(StudsAndProfsColor.values()[i])){
                  ImageView prof = new ImageView(professors.get(i));
                  prof.setFitWidth(80);
                  prof.setFitHeight(80);
                  pb.get(j).add(prof, column,row);
                  GridPane.setHalignment(prof, HPos.CENTER);
                  row++;
                }
            }

            //students in dining room

            for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
                for (int k = 0; k < receivedBoards.get(j).getDiningRoom().getStudentsByColor(StudsAndProfsColor.values()[i]); k++) {
                    ImageView st = new ImageView(students.get(i));
                    st.setFitHeight(80);
                    st.setFitWidth(80);
                    sdrb.get(j).add(st, k, i);
                    GridPane.setHalignment(st, HPos.CENTER);

                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tb.add(tb1);
        tb.add(tb2);
        tb.add(tb3);
        tb.add(tb4);


        seb.add(seb1);
        seb.add(seb2);
        seb.add(seb3);
        seb.add(seb4);


        sdrb.add(sdrb1);
        sdrb.add(sdrb2);
        sdrb.add(sdrb3);
        sdrb.add(sdrb4);


        pb.add(pb1);
        pb.add(pb2);
        pb.add(pb3);
        pb.add(pb4);

        boards.add(board1);
        boards.add(board2);
        boards.add(board3);
        boards.add(board4);

        ob.add(ob1);
        ob.add(ob2);
        ob.add(ob3);
        ob.add(ob4);

        professors.add(greenProfessor);
        professors.add(redProfessor);
        professors.add(yellowProfessor);
        professors.add(pinkProfessor);
        professors.add(blueProfessor);

        students.add(greenStudent);
        students.add(redStudent);
        students.add(yellowStudent);
        students.add(pinkStudent);
        students.add(blueStudent);


    }
}
