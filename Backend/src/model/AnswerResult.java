package model;

public class AnswerResult {
    public boolean answerIsCorrect;

    public AnswerResult( boolean answerIsCorrect ){
        this.answerIsCorrect = answerIsCorrect;
    }

    @Override
    public String toString() {
        return "Game{" +
                "answerIsCorrect=" + answerIsCorrect +
                '}';
    }
}
