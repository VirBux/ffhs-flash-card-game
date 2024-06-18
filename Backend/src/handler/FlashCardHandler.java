package handler;

import data.InMemoryDatabase;
import model.FlashCard;
import model.FlashCardPlayerAnswers;
import model.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Der FlashCardHandler verwaltet die Flashcards und die Antworten der Spieler.
 */
public class FlashCardHandler {
    private InMemoryDatabase inMemoryDatabase;
    public FlashCardHandler(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    public ArrayList<FlashCard> getAllFlashCards() {
        return this.inMemoryDatabase.getFlashCards();
    }

    public void flushAllFlashCardPlayerAnswers() {
        this.inMemoryDatabase.flushAllFlashCardPlayerAnswers();
    }

    public ArrayList<FlashCardPlayerAnswers> getAllFlashCardPlayerAnswers() {
        return this.inMemoryDatabase.getFlashCardPlayerAnswers();
    }

    public FlashCard getFlashCardByIndex(int index) {
        var flashCards = this.inMemoryDatabase.getFlashCards();
        return flashCards.get(index);
    }

    public FlashCard getFlashCardByUuid(UUID currentFlashCardUuid) {
        var flashCards = this.inMemoryDatabase.getFlashCards();
        for(FlashCard flashCardFromDb : flashCards){
            if(currentFlashCardUuid.equals(flashCardFromDb.getUuid())) return flashCardFromDb;
        }
        return null;
    }

    public void addPlayerAnswer(FlashCardPlayerAnswers flashCardPlayerAnswer){
        this.inMemoryDatabase.addFlashCardPlayerAnswer(flashCardPlayerAnswer);
    }

    /**
     * Überprüft, ob eine Spielerantwort auf eine Flashcard korrekt ist.
     *
     * @param flashCardPlayerAnswers die Spielerantwort, die überprüft werden soll
     * @return boolean ob die Antwort korrekt ist
     */
    public boolean isAnswerCorrect(FlashCardPlayerAnswers flashCardPlayerAnswers) {
        var allFlashCards = this.inMemoryDatabase.getFlashCards();
        var flashCard = this.getFlashCardByFlashCardPlayerAnswers(allFlashCards, flashCardPlayerAnswers);
        if(flashCard != null ) return flashCard.getCorrectAnswerIndex() == flashCardPlayerAnswers.getAnswerIndex();
        return false;
    }

    /**
     * Gibt eine Flashcard basierend auf einer Spielerantwort zurück.
     *
     * @param flashCards die Liste der Flashcards
     * @param flashCardPlayerAnswers die Spielerantwort
     * @return die entsprechende Flashcard oder null, wenn keine gefunden wurde
     */
    private FlashCard getFlashCardByFlashCardPlayerAnswers(ArrayList<FlashCard> flashCards, FlashCardPlayerAnswers flashCardPlayerAnswers){
        for( FlashCard flashCard : flashCards ) {
            if(flashCard.getUuid().equals(flashCardPlayerAnswers.getFlashCardUuid())) return flashCard;
        };
        return null;
    }
}
