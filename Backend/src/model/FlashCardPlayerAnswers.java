package model;

import com.fasterxml.jackson.databind.JsonNode;
import handler.ClientMessageHandler;

import java.util.UUID;

/**
 * Die FlashCardPlayerAnswers-Klasse speichert die Antworten eines Spielers auf eine Flashcard.
 */
public class FlashCardPlayerAnswers {
    private UUID userUuid;
    private UUID flashCardUuid;
    private int answerIndex;


    public FlashCardPlayerAnswers(UUID userUuid, UUID flashCardUuid) {
        this.userUuid = userUuid;
        this.flashCardUuid = flashCardUuid;
    }

    public FlashCardPlayerAnswers(ClientMessageHandler<JsonNode> clientMessage) {
        this.userUuid = UUID.fromString(clientMessage.body.get("userUuid").asText());
        this.flashCardUuid = UUID.fromString(clientMessage.body.get("flashCardUuid").asText());
        this.answerIndex = clientMessage.body.get("answerIndex").asInt();
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public UUID getFlashCardUuid() {
        return flashCardUuid;
    }

    public int getAnswerIndex() {
        return this.answerIndex;
    }

    @Override
    public String toString() {
        return "Game{" +
                "userUuid=" + userUuid +
                ", flashCardUuid=" + flashCardUuid +
                ", answerIndex=" + answerIndex +
                '}';
    }
}
