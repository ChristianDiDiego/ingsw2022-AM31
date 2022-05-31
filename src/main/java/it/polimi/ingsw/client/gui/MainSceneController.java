package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.utilities.constants.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.text.html.ListView;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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

    @FXML AnchorPane coinPane;
    @FXML Label coinLabel;
    @FXML Button charactersButton;

    ImageView[] cards;

    @FXML
    Label messageForUser;

    //This variable will store the number of students already moved somewhere from the entrance
    int numberOfMovedStudents = 0;

    int maxNumberOfMovedStudents = 0;

    int maxStepsMN = 0;
    String movedStudents = "MOVEST ";

    private int indexArchipelagoMNPosition;
    private int numberOfArchipelagos ;
    private boolean cloudClickable = false;
    private boolean cardsClickable = true;
    //Contains the first position available for each row of the dining room
    HashMap<Integer, Integer> firstPositionsAvailableDR = new HashMap<>();

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    private FXMLLoader boardSceneLoader = new FXMLLoader(getClass().getClassLoader().getResource("boardScene.fxml"));
    private FXMLLoader characterSceneLoader = new FXMLLoader(getClass().getClassLoader().getResource("characterScene.fxml"));

    Parent rootBoard;
    Scene sceneBoard;

    Parent rootCharacter;
    Scene sceneCharacter;

    private Stage stageBoard;
    private Stage stageCharacter;

    public FXMLLoader getBoardSceneLoader() {
        return boardSceneLoader;
    }

    public FXMLLoader getCharacterSceneLoader() {
        return characterSceneLoader;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    int sizeOldDeck = 0;

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
        Image[] studentColor = {studentGreen, studentRed, studentYellow, studentPink, studentBlue};
        Image whiteTower = new Image(getClass().getResourceAsStream("/images/whitetower.png"));
        Image blackTower = new Image(getClass().getResourceAsStream("/images/blacktower.png"));
        Image greyTower = new Image(getClass().getResourceAsStream("/images/greytower.png"));
        Image[] towerColor = {whiteTower, blackTower, greyTower};
        int k = 0;
        numberOfArchipelagos = listOfArchipelagos.size();

        for(int i = 0; i < listOfArchipelagos.size(); i++) {
            if(listOfArchipelagos.get(i).getIsMNPresent()) {
                ImageView mn = new ImageView(motherNature);
                mn.setFitHeight(50);
                mn.setFitWidth(50);
                singleCellArchipelago[i].getChildren().add(mn);

                indexArchipelagoMNPosition = i;
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

            if(listOfArchipelagos.get(i).getOwner() != null) {
                for(int j = 0; j < listOfArchipelagos.get(i).getBelongingIslands().size(); j++) {
                    ImageView tower = new ImageView(towerColor[listOfArchipelagos.get(i).getOwner().getColorOfTowers().ordinal()]);
                    tower.setFitHeight(40);
                    tower.setFitWidth(35);
                    singleCellArchipelago[i].getChildren().add(tower);
                }
            }

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
            System.out.println(maxNumberOfMovedStudents);
            /* data dropped */
            System.out.println("onDragDropped");
            boolean success = false;
            try {
                /* if there is a string data on dragboard, read it and use it */
                if(event.getDragboard().getString().equals("mn")){
                    if(maxStepsMN == 0){
                        //If the maxSteps is 0 it means that we are not in moveMN phase
                        event.setDropCompleted(false);
                        event.consume();
                        return;
                    }
                    int steps = calculateSteps(target.getAccessibleText());
                    System.out.println("The max step is " + maxStepsMN + "and it is trying to do " + steps);
                    if(steps <= maxStepsMN){
                        ImageView mn = new ImageView(event.getDragboard().getImage());
                        mn.setFitHeight(80);
                        mn.setFitWidth(80);
                        target.getChildren().add(mn);
                        playMoveMn(steps);
                        maxStepsMN = 0;
                        cloudClickable = true;
                        System.out.println("cloud clickable setted to " + cloudClickable);
                        event.setDropCompleted(true);
                    }else {
                        event.setDropCompleted(false);
                    }

                    event.consume();
                    return;
                }
                if(maxNumberOfMovedStudents == 0){
                    //If the maxSteps is 0 it means that we are not in moveST phase
                    event.setDropCompleted(false);
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
                if(numberOfMovedStudents == maxNumberOfMovedStudents){

                    playMoveStudents(movedStudents);
                    movedStudents = "MOVEST ";
                    numberOfMovedStudents = 0;
                    maxNumberOfMovedStudents = 0;
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

    private int calculateSteps(String mnDestination){
        int indexMnDestination = 0;
        //Find the index of the destination
        for(indexMnDestination= 0; indexMnDestination< singleCellArchipelago.length; indexMnDestination++){
            if(singleCellArchipelago[indexMnDestination].getAccessibleText().equals(mnDestination)){
                break;
            }
        }

        int mnSteps;
        if(indexMnDestination < indexArchipelagoMNPosition){
            int invisibleArc = 0;
            //Count the archipelagos that are not visible between the destination and the current position
            //(They are only at the end of the singlCellArchipelago array
            for(int i = indexArchipelagoMNPosition; i< singleCellArchipelago.length; i++){
                    if(!singleCellArchipelago[i].isVisible()) invisibleArc++;
            }
            //calculate the distance from the current island to the the one that should be the last island if every island would be displayed
            //remove from this the number of arc that are not visible
            //add the index of the destination ( is like if calculating the steps from 0 to the destination)
            // +1 because the index stars from 0
            mnSteps = (Constants.NUMBEROFISLANDS - indexArchipelagoMNPosition) - invisibleArc + indexMnDestination +1;

        }else {
             mnSteps = indexMnDestination - indexArchipelagoMNPosition;
        }

        return mnSteps;
    }
    private void playMoveMn(int mnSteps){

        String moveMn = "MOVEMN " + mnSteps;
        support.firePropertyChange("moveMn", "", moveMn );
    }

    private void setOnClickCardListener(ImageView card){
        card.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("Card pressed " + card.getAccessibleText());
                System.out.println("Cards clickable status " + cardsClickable);
                if(cardsClickable){
                    playCard(card.getAccessibleText());
                    card.setOpacity(0.5);
                }

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
            firstPositionsAvailableDR.put(i, receivedBoard.getDiningRoom().getStudentsByColor(StudsAndProfsColor.values()[i]));
        };
        //Adding StackPane to every Cell in the GridPane and Adding the Target Events to each StackPane.
        for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
            for(int j= 0; j< Constants.MAXSTUDENTSINDINING; j++){
                StackPane stackPane = new StackPane();
                stackPane.setPrefSize(100, 100);
                setOnDragOverDiningRoom(stackPane);
                setOnDragDroppedOnDiningRoom(stackPane);
                studentsInDR.add(stackPane, j, i);
            }
        }

        //students in entrance
        int column = 0;
        int row = 0;
        for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
            for(int j = 0; j < receivedBoard.getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]); j++ ){
                if(row == 0 && column==0){
                    column++;
                }
                ImageView st =new ImageView(studentsImages[i]);
                st.setFitWidth(120);
                st.setFitHeight(120);
                st.setAccessibleText(""+i);
                setOnDragStudentDetected(st);
                setOnDragImageDone(st);
                studentsInEntrance.add(st, column, row);
                column++;
                if(column == 2){
                    column=0;
                    row++;
                }
            }
        }

    }

    private void setOnDragOverDiningRoom(StackPane diningRoom){
        diningRoom.setOnDragOver((DragEvent event) -> {
            /* data is dragged over the target */
            //  System.out.println("onDragOver");
            if(event.getDragboard().hasString()){
                event.acceptTransferModes(TransferMode.ANY);
            }
            /* accept it only if it has a string data */

            event.consume();
        });
    }
    private void setOnDragDroppedOnDiningRoom(StackPane diningRoomTarget)
    {
        diningRoomTarget.setOnDragDropped((DragEvent event) -> {
            /* data dropped */
            System.out.println("onDragDropped");
            boolean success = false;


            try {
                /* if there is a string data on dragboard, read it and use it */
                if(event.getDragboard().hasString()){
                    if(maxNumberOfMovedStudents == 0){
                        //If the maxSteps is 0 it means that we are not in moveST phase
                        event.setDropCompleted(false);
                        event.consume();
                        return;
                    }

                    //Getting the coordinate of the target on the diningRoom
                    Integer cIndex = GridPane.getColumnIndex(diningRoomTarget);
                    Integer rIndex = GridPane.getRowIndex(diningRoomTarget);
                    int x = cIndex == null ? 0 : cIndex;
                    int kingdomTarget = rIndex == null ? 0 : rIndex;

                    ImageView st = new ImageView(event.getDragboard().getImage());
                    st.setFitHeight(80);
                    st.setFitWidth(80);
                    int kingdomOfTheStudent = Integer.parseInt(event.getDragboard().getString());
                    //allow the movemnt only if the row is the one of the student
                    if(kingdomTarget == kingdomOfTheStudent && firstPositionsAvailableDR.get(kingdomTarget) < Constants.MAXSTUDENTSINDINING){
                        //add(whatToAdd, column, row
                            studentsInDR.add(st, firstPositionsAvailableDR.get(kingdomTarget), kingdomOfTheStudent);
                            firstPositionsAvailableDR.put(kingdomTarget, firstPositionsAvailableDR.get(kingdomTarget)+1);

                        String colorMoved = convertTextNumberToColor(event.getDragboard().getString());
                        String destination = ""+0;
                        movedStudents += colorMoved + "-" + destination;
                        numberOfMovedStudents++;
                        System.out.println(movedStudents);
                        if(numberOfMovedStudents == 3){

                            playMoveStudents(movedStudents);
                            movedStudents = "MOVEST ";
                            numberOfMovedStudents = 0;
                            maxNumberOfMovedStudents = 0;
                        }else{
                            movedStudents +=",";
                        }
                            event.setDropCompleted(true);
                            event.consume();

                    }else{
                        event.setDropCompleted(false);
                        event.consume();
                    }


                }

            }catch (Exception e){
                e.printStackTrace();
            }

        });
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
                        st.setFitHeight(100);
                        st.setFitWidth(100);
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
                System.out.println("Cloud clickable " + cloudClickable);
                if(cloudClickable){
                    playCloud(cloud.getAccessibleText());
                    cloudClickable = false;
                    System.out.println("Cloud clickable setted to" + cloudClickable);
                }

                event.consume();
            }
        });
    }

    private void playCloud(String cloudSelected){
        String playSelectedCloud = "CLOUD " + cloudSelected;
        support.firePropertyChange("cloudPlayed", "", playSelectedCloud );

    }
    public void printDeck(Deck deck) {
        for(ImageView card : cards){
            card.setVisible(false);
        }

        for(Card c : deck.getLeftCards()) {
            System.out.println("Card power: " + c.getPower());
            cards[deck.getLeftCards().indexOf(c)].setImage(assistants[c.getPower() - 1]);
            cards[deck.getLeftCards().indexOf(c)].setAccessibleText(""+c.getPower());
            cards[deck.getLeftCards().indexOf(c)].setVisible(true);
            setOnClickCardListener(cards[deck.getLeftCards().indexOf(c)]);
        }
        if(deck.getLeftCards().size() < sizeOldDeck){
            for(ImageView card : cards){
                card.setOpacity(1.0);
            }
        }
        sizeOldDeck = deck.getLeftCards().size();
    }

    public void openBoardScene(ActionEvent event) throws IOException {
        if(stageBoard == null) {
            stageBoard = new Stage();
            sceneBoard = new Scene(rootBoard);
            stageBoard.setScene(sceneBoard);
        }

        if(stageBoard.isShowing() == false) {
            stageBoard.show();
        }
    }

    public void openCharactersScene(ActionEvent event) throws IOException{
        if(stageCharacter == null) {
            stageCharacter = new Stage();
            sceneCharacter = new Scene(rootCharacter);
            stageCharacter.setScene(sceneCharacter);
        }
        if(stageCharacter.isShowing() == false) {
            stageCharacter.show();
        }
    }

    public void printCoin(int coins) {
        coinPane.setVisible(true);
        coinLabel.setVisible(true);
        String coin = Integer.toString(coins);
        coinLabel.setText("x" + coin);
        charactersButton.setVisible(true);
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
        profRed.setImage(redProfessor);
        profYellow.setImage(yellowProfessor);
        profPink.setImage(pinkProfessor);
        profBlue.setImage(blueProfessor);

        professors.add(profGreen);
        professors.add(profRed);
        professors.add(profYellow);
        professors.add(profPink);
        professors.add(profBlue);

        clouds.add(cloud1);
        clouds.add(cloud2);
        clouds.add(cloud3);
        clouds.add(cloud4);

        try {
            rootBoard = boardSceneLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            rootCharacter = characterSceneLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        coinPane.setVisible(false);
        coinLabel.setVisible(false);
        charactersButton.setVisible(false);
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

    public void setMaxStepsMN(int maxStepsMN){
        this.maxStepsMN = maxStepsMN;
    }

    public void setMaxNumberOfMovedStudents(int maxNumberOfMovedStudents){
        this.maxNumberOfMovedStudents = maxNumberOfMovedStudents;
    }
    public void setCardsClickable(boolean isCardClickable){
        System.out.println("card clickable setted to " + isCardClickable);
        this.cardsClickable = isCardClickable;
    }

    public void setMessageForUserText(String messageForUser){
        this.messageForUser.setText(messageForUser);
    }
}
