package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.utilities.Constants;
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

import java.net.URL;
import java.util.*;

/**
 * This class contains the controller to manage the board scene
 */
public class BoardSceneController implements Initializable {
    Image blackTower = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/blacktower.png")));
    Image whiteTower = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/whitetower.png")));
    Image greyTower = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/greytower.png")));
    Image greenProfessor = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/profgreen.png")));
    Image redProfessor = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/profred.png")));
    Image yellowProfessor = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/profyellow.png")));
    Image pinkProfessor = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/profpink.png")));
    Image blueProfessor = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/profblue.png")));
    List<Image> professors = new ArrayList<>();
    Image greenStudent = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentgreen.png")));
    Image redStudent = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentred.png")));
    Image yellowStudent = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentyellow.png")));
    Image pinkStudent = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentpink.png")));
    Image blueStudent = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/professorsAndStudents/studentblue.png")));
    List<Image> students = new ArrayList<>();
    @FXML
    AnchorPane board1;
    @FXML
    AnchorPane board2;
    @FXML
    AnchorPane board3;
    @FXML
    AnchorPane board4;
    List<AnchorPane> boards = new ArrayList<>();

    @FXML
    GridPane seb1;
    @FXML
    GridPane seb2;
    @FXML
    GridPane seb3;
    @FXML
    GridPane seb4;
    List<GridPane> seb = new ArrayList<>();


    @FXML
    GridPane sdrb1;
    @FXML
    GridPane sdrb2;
    @FXML
    GridPane sdrb3;
    @FXML
    GridPane sdrb4;
    List<GridPane> sdrb = new ArrayList<>();

    @FXML
    GridPane tb1;
    @FXML
    GridPane tb2;
    @FXML
    GridPane tb3;
    @FXML
    GridPane tb4;

    List<GridPane> tb = new ArrayList<>();

    @FXML
    Label ob1;
    @FXML
    Label ob2;
    @FXML
    Label ob3;
    @FXML
    Label ob4;

    List<Label> ob = new ArrayList<>();

    @FXML
    GridPane pb1;
    @FXML
    GridPane pb2;
    @FXML
    GridPane pb3;
    @FXML
    GridPane pb4;

    List<GridPane> pb = new ArrayList<>();

    Font font = Font.font("Calisto MT", FontPosture.ITALIC, 18);

    private List<Board> receivedBoards = new ArrayList<>();

    /**
     * Set the boards received from GUI class
     *
     * @param boards sent by the server
     */
    public void setReceivedBoards(List<Board> boards) {
        this.receivedBoards = boards;
    }

    /**
     * For all players writes near the name the card used in the current turn
     *
     * @param players sent by the server
     */
    public void printLastUsedCard(List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getLastUsedCard() != null) {
                String nickname = ob.get(i).getText();
                String lastUsedCard = ", last used card: " + players.get(i).getLastUsedCard().getPower() + "   " + players.get(i).getLastUsedCard().getMaxSteps();
                ob.get(i).setText(nickname + lastUsedCard);
                ob.get(i).setFont(font);
            }
        }
    }

    /**
     * For all player shows the personal board
     */
    public void showAllBoards() {

        switch (receivedBoards.size()) {
            case 2: {
                board3.setVisible(false);
                board4.setVisible(false);
                ob3.setVisible(false);
                ob4.setVisible(false);
            }
            case 3: {
                board4.setVisible(false);
                ob4.setVisible(false);
            }
            case 4:
        }

        for (int j = 0; j < receivedBoards.size(); j++) {
            ob.get(j).setText("Player: " + receivedBoards.get(j).getNickname());
            ob.get(j).setFont(font);

            seb.get(j).getChildren().clear();
            sdrb.get(j).getChildren().clear();
            tb.get(j).getChildren().clear();
            pb.get(j).getChildren().clear();

            //towers
            int row = 0;
            int column = 0;
            for (int i = 0; i < receivedBoards.get(j).getTowersOnBoard().getNumberOfTowers(); i++) {
                ImageView t;
                switch (receivedBoards.get(j).getColorOfTower()) {
                    case WHITE -> t = new ImageView(whiteTower);
                    case GREY -> t = new ImageView(greyTower);
                    default -> t = new ImageView(blackTower);
                }
                t.setFitHeight(40);
                t.setFitWidth(40);
                tb.get(j).add(t, column, row);
                GridPane.setHalignment(t, HPos.CENTER);
                column++;
                if (column == 2) {
                    column = 0;
                    row++;
                }
            }

            //students in entrance
            column = 0;
            row = 0;
            for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
                for (int k = 0; k < receivedBoards.get(j).getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]); k++) {
                    if (row == 0 && column == 0) {
                        column++;
                    }
                    ImageView st = new ImageView(students.get(i));
                    st.setFitWidth(25);
                    st.setFitHeight(25);

                    seb.get(j).add(st, column, row);
                    GridPane.setHalignment(st, HPos.CENTER);
                    column++;
                    if (column == 2) {
                        column = 0;
                        row++;
                    }
                }
            }

            //professors
            row = 0;
            column = 0;
            for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
                if (receivedBoards.get(j).getProfessorsTable().getHasProf(StudsAndProfsColor.values()[i])) {
                    ImageView prof = new ImageView(professors.get(i));
                    prof.setFitWidth(25);
                    prof.setFitHeight(25);
                    pb.get(j).add(prof, column, row);
                    GridPane.setHalignment(prof, HPos.CENTER);
                }
                row++;
            }

            //students in dining room
            for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
                for (int k = 0; k < receivedBoards.get(j).getDiningRoom().getStudentsByColor(StudsAndProfsColor.values()[i]); k++) {
                    ImageView st = new ImageView(students.get(i));
                    st.setFitHeight(25);
                    st.setFitWidth(25);
                    sdrb.get(j).add(st, k, i);
                    GridPane.setHalignment(st, HPos.CENTER);

                }
            }
        }
    }

    /**
     * Override of the method initialize to set all the components of the scene
     * according to the game in progress
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tb.addAll(Arrays.asList(tb1, tb2, tb3, tb4));

        seb.addAll(Arrays.asList(seb1, seb2, seb3, seb4));

        sdrb.addAll(Arrays.asList(sdrb1, sdrb2, sdrb3, sdrb4));

        pb.addAll(Arrays.asList(pb1, pb2, pb3, pb4));

        boards.addAll(Arrays.asList(board1, board2, board3, board4));

        ob.addAll(Arrays.asList(ob1, ob2, ob3, ob4));

        professors.addAll(Arrays.asList(greenProfessor, redProfessor, yellowProfessor, pinkProfessor, blueProfessor));

        students.add(greenStudent);
        students.add(redStudent);
        students.add(yellowStudent);
        students.add(pinkStudent);
        students.add(blueStudent);
    }
}
