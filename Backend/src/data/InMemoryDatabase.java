package data;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.FlashCard;
import model.FlashCardPlayerAnswers;
import model.Player;
import org.java_websocket.WebSocket;
import util.JsonToFlashCards;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Die InMemoryDatabase-Klasse verwaltet die Flashcards, Spielerantworten und Spieler im Speicher.
 */
public class InMemoryDatabase {
    private final ArrayList<FlashCard> flashCards;
    private final ArrayList<FlashCardPlayerAnswers> flashCardPlayerAnswers = new ArrayList<>();
    private final Set<Player> players = new HashSet<>();

    /**
     * Konstruktor, der die Flashcards aus einer JSON-Datei lädt.
     */
    public InMemoryDatabase() {
        this.flashCards = JsonToFlashCards.loadFlashCards(
                "../resources/questions/ftoop_multiplayerquiz_fragenkatalog_2021.json"
        );
    }

    // Setter
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void addFlashCardPlayerAnswer(FlashCardPlayerAnswers flashCardPlayerAnswers){
        this.flashCardPlayerAnswers.add(flashCardPlayerAnswers);
    }

    public void flushAllFlashCardPlayerAnswers() {
        this.flashCardPlayerAnswers.clear();
    }

    // Getter
    public ArrayList<FlashCard> getFlashCards() {
        return flashCards;
    }

    public ArrayList<FlashCardPlayerAnswers> getFlashCardPlayerAnswers() {
        return flashCardPlayerAnswers;
    }

    public Set<Player> getAllPlayers() {
        return players;
    }

    // Delete
    public void removePlayer(Player playerToRemove) {
        this.players.removeIf(player -> player.equals(playerToRemove));
    }
}
