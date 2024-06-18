package model;

import java.util.ArrayList;
import java.util.UUID;

public class FlashCard {
    private UUID uuid;
    private String question;
    private ArrayList<String> answers;
    private int correctAnswerIndex;

    // After creation, readonly
    public FlashCard(String question, ArrayList<String> answers, int correctAnswerIndex) {
        this.uuid = UUID.randomUUID();
        this.question = question;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    // Getter
    public UUID getUuid() {
        return uuid;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    @Override
    public String toString() {
        return "FlashCard{" +
                "uuid=" + uuid +
                ", question='" + question + '\'' +
                ", answers=" + answers +
                ", correctAnswerIndex=" + correctAnswerIndex +
                '}';
    }
}
