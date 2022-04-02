package it.polimi.ingsw.model.expertMode;

public abstract class Characters {

    private  int price;
    private boolean alreadyUsed;

    public Characters(int price){
        alreadyUsed = false;
        this.price = price;
    }

    public abstract void usePower();

    public int getPrice(){
        return this.price;
    }

    public  boolean getAlreadyUsed() {
        return this.alreadyUsed;
    }
}
