package model;

public class Game {
    private int currentCardIndex = 0;
    private boolean gameIsRunning = false;

    public void runGame(){
        this.gameIsRunning = true;
    }

    public void stopGame() {
        this.gameIsRunning = false;
    }

    public void incrementCardIndex(){
        this.currentCardIndex ++;
    }

    public int getCurrentCardIndex() {
        return currentCardIndex;
    }

    public boolean isGameIsRunning() {
        return gameIsRunning;
    }

    @Override
    public String toString() {
        return "Game{" +
                "currentCardIndex=" + currentCardIndex +
                ", gameIsRunning=" + gameIsRunning +
                '}';
    }
}
