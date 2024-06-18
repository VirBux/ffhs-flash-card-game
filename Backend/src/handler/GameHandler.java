package handler;

import data.InMemoryDatabase;
import main.Controller;
import model.*;
import org.java_websocket.WebSocket;


import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Der GameHandler verwaltet das Spiel, einschliesslich der Spieler, Flashcards und deren Antworten.
 * Über ihn laufen alle Commandos. Die werden weitergeleitet an die unterstehenden Handlers.
 */
public class GameHandler {
    private PlayerHandler playerHandler;
    private FlashCardHandler flashCardHandler;
    private Game game;

    public GameHandler(
            FlashCardHandler flashCardHandler,
            PlayerHandler playerHandler
    ) {
        this.flashCardHandler = flashCardHandler;
        this.playerHandler = playerHandler;
    }

    /**
     * Startet das Spiel und initialisiert die erforderlichen Daten.
     *
     * @param connections die Menge der WebSocket-Verbindungen der Spieler
     */
    public void startGame(Set<WebSocket> connections) {
        System.out.println("Starte Spiel");

        // Reset Data
        this.playerHandler.setAllToPlaying();
        this.playerHandler.resetAllPoints();
        this.flashCardHandler.flushAllFlashCardPlayerAnswers();

        // Init new game
        this.game = new Game();
        this.game.runGame();

        this.nextFlashCard();
    }

    /**
     * Fügt die Antwort eines Spielers auf eine Flashcard hinzu und überprüft deren Korrektheit.
     *
     * @param flashCardPlayerAnswer die Antwort des Spielers auf eine Flashcard
     */
    public void playerAddAnswer(FlashCardPlayerAnswers flashCardPlayerAnswer){
        this.flashCardHandler.addPlayerAnswer(flashCardPlayerAnswer);
        boolean isCorrect = this.flashCardHandler.isAnswerCorrect(flashCardPlayerAnswer);

        var player = this.playerHandler.findPlayerByUuid(flashCardPlayerAnswer.getUserUuid());

        if( isCorrect ) {
            System.out.println("Ein Spieler hat eine Korrekte Antwort abgegeben!");
            this.handleCorrectAnswer(player);
        } else {
            this.handleWrongAnswer(player);

            if ( this.hasUserToAnswer(flashCardPlayerAnswer.getFlashCardUuid()) == false ) {
                this.nextFlashCard();
                System.out.println("Kein Spieler hat eine richtige Antwort abgegeben");
            }
        }

        this.sendCurrentOnlinePlayers();
    }

    /**
     * Lädt die nächste Flashcard und setzt das Spiel fort.
     */
    public void nextFlashCard(){
        System.out.println("Lade nächste Karte");
        FlashCard currentFlashCard = null;

        try {
            currentFlashCard = this.flashCardHandler.getFlashCardByIndex(this.game.getCurrentCardIndex());
            this.game.incrementCardIndex();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Keine Karten mehr verfügbar");
        } finally {
            if(currentFlashCard != null){
                var serverMessage = new ServerMessageHandler<>("nextFlashCardServer", currentFlashCard);
                Controller.broadcastToAll(this.playerHandler.getAllPlayingConnection(), serverMessage);
            } else {
                this.endGame();
            }
        }

        this.sendCurrentOnlinePlayers();
    }

    public Game getCurrentGame() {
        return this.game;
    }

    public void resetGameIfNoPlayerArePlaying() {
        if(this.playerHandler.getAllPlayingConnection().isEmpty()) this.endGame();
    }

    private void endGame() {
        if(this.game != null) this.game.stopGame();
        System.out.println("Beende Spiel");
        this.sendGameResults();
        this.playerHandler.unsetAllPlaying();
    }

    private void sendCurrentOnlinePlayers() {
        var players = this.playerHandler.getAllPlayersFromDb();

        Controller.broadcastToAll(
                this.playerHandler.getAllPlayingConnection(),
                new ServerMessageHandler<Set<Player>>("currentOnlineUsersServer", players)
        );
    }

    private void sendGameResults() {
        var players = this.playerHandler.getAllPlayersFromDb();

        Controller.broadcastToAll(
                this.playerHandler.getAllPlayingConnection(),
                new ServerMessageHandler<Set<Player>>("noMoreCardsShowResultsServer", players)
        );

        Controller.broadcastToAll(
                this.playerHandler.getAllConnection(),
                new ServerMessageHandler<Game>("serverCurrentGame", this.getCurrentGame())
        );
    }

    private void handleCorrectAnswer(Player player){
        // Add Point
        player.addPoints(1);
        // Inform Correct answer User
        var serverMessage = new ServerMessageHandler<AnswerResult>("answerResultServer", new AnswerResult(true));
        Controller.sendMessage(player.getConnection(), serverMessage);

        this.nextFlashCard();
        System.out.println("Spieler " + player.getUserName() + " hat die Korrekte Antwort gegeben");
    }

    private void handleWrongAnswer(Player player){
        var serverMessage = new ServerMessageHandler<AnswerResult>("answerResultServer", new AnswerResult(false));
        Controller.sendMessage(player.getConnection(), serverMessage);
    }

    /**
     * Überprüft, ob noch Spieler eine Antwort auf die aktuelle Flashcard geben müssen.
     *
     * @param currentFlashCardUuid die UUID der aktuellen Flashcard
     * @return boolean, ob noch Spieler antworten müssen
     */
    private boolean hasUserToAnswer(UUID currentFlashCardUuid) {
        var currentFlashCard = this.flashCardHandler.getFlashCardByUuid(currentFlashCardUuid);
        var flashCardPlayerAnswers = this.flashCardHandler.getAllFlashCardPlayerAnswers();
        System.out.println("Total Antworten in der InMemoryDatabase: " + flashCardPlayerAnswers.size());

        var filteredAnswers = new ArrayList<FlashCardPlayerAnswers>();
        for( FlashCardPlayerAnswers flashCardPlayerAnswer : flashCardPlayerAnswers ){
            if( flashCardPlayerAnswer.getFlashCardUuid().equals(currentFlashCard.getUuid()) ) filteredAnswers.add(flashCardPlayerAnswer);
        }

        int givenAnswersCount = filteredAnswers.size();
        int connectionCount = this.playerHandler.getAllPlayingConnection().size();
        System.out.println("Aktive Spielerzahl: " + connectionCount + " | Gegebene Antworten: " + givenAnswersCount);

        return connectionCount != givenAnswersCount;
    }

}
