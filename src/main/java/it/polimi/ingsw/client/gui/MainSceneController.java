package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.utilities.constants.Constants;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import javax.swing.text.html.ListView;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class MainSceneController implements Initializable {
    private List<ImageView> towers = new ArrayList<>();
    private List<ImageView> professors = new ArrayList<>();
    private List<GridPane> clouds = new ArrayList<>();

    FlowPane[] singleCellArchipelago;
    Image greenStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentgreen.png"));
    Image redStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentred.png"));
    Image yellowStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentyellow.png"));
    Image pinkStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentpink.png"));
    Image blueStudent = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentblue.png"));
    Image[] studentsImages = {greenStudent,redStudent,yellowStudent,pinkStudent,blueStudent};

    Image assistant1 = new Image(getClass().getResourceAsStream("/images/assistants/Assistente1.png"));
    Image assistant2 = new Image(getClass().getResourceAsStream("/images/assistants/Assistente2.png"));
    Image assistant3 = new Image(getClass().getResourceAsStream("/images/assistants/Assistente3.png"));
    Image assistant4 = new Image(getClass().getResourceAsStream("/images/assistants/Assistente4.png"));
    Image assistant5 = new Image(getClass().getResourceAsStream("/images/assistants/Assistente5.png"));
    Image assistant6 = new Image(getClass().getResourceAsStream("/images/assistants/Assistente6.png"));
    Image assistant7 = new Image(getClass().getResourceAsStream("/images/assistants/Assistente7.png"));
    Image assistant8 = new Image(getClass().getResourceAsStream("/images/assistants/Assistente8.png"));
    Image assistant9 = new Image(getClass().getResourceAsStream("/images/assistants/Assistente9.png"));
    Image assistant10 = new Image(getClass().getResourceAsStream("/images/assistants/Assistente10.png"));
    Image[] assistants = {assistant1,assistant2,assistant3,assistant4,assistant5,assistant6,assistant7,assistant8,assistant9,assistant10};

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
    @FXML ImageView card1;
    @FXML ImageView card2;
    @FXML ImageView card3;
    @FXML ImageView card4;
    @FXML ImageView card5;
    @FXML ImageView card6;
    @FXML ImageView card7;
    @FXML ImageView card8;
    @FXML ImageView card9;
    @FXML ImageView card10;

    ImageView[] cards;

    //This variable will store the number of students already moved somewhere from the entrance
    int numberOfMovedStudents = 0;
    String movedStudents = "MOVEST ";

    int idArchipelagoMNPosition;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }


    public void printArchipelagos(List<Archipelago> listOfArchipelagos) {
        for(FlowPane f : singleCellArchipelago) {
            f.getChildren().clear();
        }
        Image motherNature = new Image(getClass().getResourceAsStream("/images/mothernature.png"));
        Image studentRed = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentred.png"));
        Image studentGreen = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentgreen.png"));
        Image studentYellow = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentyellow.png"));
        Image studentPink = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentpink.png"));
        Image studentBlue = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/studentblue.png"));
        Image[] studentColor = {studentRed, studentGreen, studentYellow, studentPink, studentBlue};
        int k = 0;
        for(int i = 0; i < listOfArchipelagos.size(); i++) {
            if(listOfArchipelagos.get(i).getIsMNPresent()) {
                ImageView mn = new ImageView(motherNature);
                mn.setFitHeight(50);
                mn.setFitWidth(50);
                singleCellArchipelago[i].getChildren().add(mn);

                idArchipelagoMNPosition = listOfArchipelagos.get(i).getIdArchipelago();
                mn.setAccessibleText("mn");
                setOnDragMNDetected(mn);
                setOnDragImageDone(mn);

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
            singleCellArchipelago[i].setAccessibleText(""+listOfArchipelagos.get(i).getIdArchipelago());
            setOnDragOverArchipelago(singleCellArchipelago[i]);
            setOnDragDroppedOnArchipelago(singleCellArchipelago[i]);
            k++;
        }
        for(int i = k; i < Constants.NUMBEROFISLANDS; i++) {
            singleCellArchipelago[i].setVisible(false);
        }

    }

    /**
     * Called when the user do a click-left on a student
     * @param student
     */
    private void setOnDragStudentDetected(ImageView student)
    {
        student.setOnDragDetected((MouseEvent event) -> {
            /* drag was detected, start drag-and-drop gesture*/
            System.out.println("onDragDetected");
            System.out.println(student.getAccessibleText());

            /* allow any transfer mode */
            Dragboard db = student.startDragAndDrop(TransferMode.MOVE);
            //Shows an image of the student that is moveing
            db.setDragView(student.getImage());

            /* put a string on dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString(student.getAccessibleText());
            content.putImage(student.getImage());
            db.setContent(content);

            event.consume();
        });
    }

    /**
     *
     * @param target
     */
    private void setOnDragOverArchipelago(FlowPane target){
        target.setOnDragOver((DragEvent event) -> {
            /* data is dragged over the target */
          //  System.out.println("onDragOver");
            if(event.getDragboard().hasString()){
                event.acceptTransferModes(TransferMode.ANY);
            }
            /* accept it only if it has a string data */

            event.consume();
        });
    }

    private void setOnDragDroppedOnArchipelago(FlowPane target)
    {
        target.setOnDragDropped((DragEvent event) -> {
            /* data dropped */
            System.out.println("onDragDropped");
            boolean success = false;
            try {
                /* if there is a string data on dragboard, read it and use it */
                if(event.getDragboard().getString().equals("mn")){
                    ImageView mn = new ImageView(event.getDragboard().getImage());
                    mn.setFitHeight(80);
                    mn.setFitWidth(80);
                    target.getChildren().add(mn);
                    playMoveMn(target.getAccessibleText());

                    event.setDropCompleted(true);
                    event.consume();
                    return;
                }

                ImageView st = new ImageView(event.getDragboard().getImage());
                st.setFitHeight(80);
                st.setFitWidth(80);
                target.getChildren().add(st);

                String colorMoved = convertTextNumberToColor(event.getDragboard().getString());
                String destination = target.getAccessibleText();
                movedStudents += colorMoved + "-" + destination;
                numberOfMovedStudents++;
                System.out.println(movedStudents);
                if(numberOfMovedStudents == 3){

                    playMoveStudents(movedStudents);
                    movedStudents = "MOVEST ";
                    numberOfMovedStudents = 0;
                }else{
                    movedStudents +=",";
                }
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(true);
                event.consume();
            }catch (Exception e){
                e.printStackTrace();
            }

        });
    }

    private void setOnDragImageDone(ImageView image){
        image.setOnDragDone((DragEvent event) -> {
            /* the drag-and-drop gesture ended */
            /* if the data was successfully moved, clear it */
            if (event.getTransferMode() == TransferMode.MOVE) {
                image.setVisible(false);
            }

            event.consume();
        });
    }

    private void playMoveStudents(String movedStudents){
        support.firePropertyChange("movedStudents", "", movedStudents );
    }
    private void playMoveMn(String mnDestination){
        int idMnDestination = Integer.parseInt(mnDestination);
        int mnSteps = idMnDestination - idArchipelagoMNPosition;
        String moveMn = "MOVEMN " + mnSteps;
        support.firePropertyChange("moveMn", "", moveMn );
    }
    private void setOnClickCardListener(ImageView card){
        card.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("Card pressed " + card.getAccessibleText());
                playCard(card.getAccessibleText());

                event.consume();
            }
        });
    }


    private void playCard(String cardPower){
        String playSelectedCard = "CARD " + cardPower;
        support.firePropertyChange("cardPlayed", "", playSelectedCard );
    }

    public void setOnDragMNDetected(ImageView motherNature)
    {
        motherNature.setOnDragDetected((MouseEvent event) -> {
            /* drag was detected, start drag-and-drop gesture*/
            System.out.println("onDragDetected");
            System.out.println(motherNature.getAccessibleText());

            /* allow any transfer mode */
            Dragboard db = motherNature.startDragAndDrop(TransferMode.ANY);
            //Shows an image of the motherNature that is moveing
            db.setDragView(motherNature.getImage());

            /* put a string on dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString(motherNature.getAccessibleText());
            content.putImage(motherNature.getImage());
            db.setContent(content);

            event.consume();
        });
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
        int column = 0;
        int row = 0;
        for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
            for(int j = 0; j < receivedBoard.getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]); j++ ){
                if(row == 0 && column==0){
                    column++;
                }
                ImageView st =new ImageView(studentsImages[i]);
                System.out.println("Image setted for kingdom " + i);
                st.setFitWidth(120);
                st.setFitHeight(120);
                st.setAccessibleText(""+i);
                setOnDragStudentDetected(st);
                setOnDragImageDone(st);
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

                clouds.get(cloudList.indexOf(c)).setAccessibleText(""+c.getIdCloud());
                setOnClickCloud(clouds.get(cloudList.indexOf(c)));
            }
        }
    }

    private void setOnClickCloud(GridPane cloud){
        cloud.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("Cloud pressed " + cloud.getAccessibleText());
                playCloud(cloud.getAccessibleText());

                event.consume();
            }
        });
    }

    private void playCloud(String cloudSelected){
        String playSelectedCloud = "CLOUD " + cloudSelected;
        support.firePropertyChange("cloudPlayed", "", playSelectedCloud );

    }
    public void printDeck(Deck deck) {

        for(Card c : deck.getLeftCards()) {
            cards[deck.getLeftCards().indexOf(c)].setImage(assistants[c.getPower() - 1]);
            cards[deck.getLeftCards().indexOf(c)].setAccessibleText(""+c.getPower());
            setOnClickCardListener(cards[deck.getLeftCards().indexOf(c)]);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image tower = new Image(getClass().getResourceAsStream("/images/tower.png"));
        Image greenProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profgreen.png"));
        Image redProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profred.png"));
        Image yellowProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profyellow.png"));
        Image pinkProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profpink.png"));
        Image blueProfessor = new Image(getClass().getResourceAsStream("/images/professorsAndStudents/profblue.png"));

        cards = new ImageView[]{card1, card2, card3, card4, card5, card6, card7, card8, card9, card10};

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

    /**
     * Convert the number of the color of the student provided to the initial letter of the color
     */
    private String convertTextNumberToColor(String textNumberOfTheColor){

        int numberOfTheColor = Integer.parseInt(textNumberOfTheColor);
        switch (numberOfTheColor){
            case 0 -> {return "G";}
            case 1 -> {return "R";}
            case 2 -> {return "Y";}
            case 3 -> {return "P";}
            case 4 -> {return "B";}
        }
        return null;
    }
}
