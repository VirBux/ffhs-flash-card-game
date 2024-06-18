package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.FlashCard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Die JsonToFlashCards-Klasse bietet eine Methode zum Laden von FlashCards aus einer JSON-Datei.
 */
public class JsonToFlashCards {
    /**
     * Lädt FlashCards aus einer JSON-Datei.
     *
     * @param filePath der Pfad zur JSON-Datei
     * @return eine Liste von FlashCards
     */
    public static ArrayList<FlashCard> loadFlashCards(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<FlashCard> flashCards = new ArrayList<>();

        try {
            // Load JSON-File
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            for (JsonNode dateNode : rootNode) {
                JsonNode questionsNode = dateNode.get("questions");
                if (questionsNode != null) {
                    for (JsonNode questionNode : questionsNode) {
                        // Get Data
                        String question = questionNode.get("question").asText();
                        String correctAnswer = questionNode.get("correct").asText();
                        JsonNode optionsNode = questionNode.get("options");

                        ArrayList<String> answers = new ArrayList<>();
                        Iterator<String> fieldNames = optionsNode.fieldNames();

                        while (fieldNames.hasNext()) {
                            String key = fieldNames.next();
                            answers.add(optionsNode.get(key).asText());
                        }

                        int correctAnswerIndex = switch (correctAnswer) {
                            case "A" -> 0;
                            case "B" -> 1;
                            case "C" -> 2;
                            default -> throw new IllegalArgumentException("Unexpected value: " + correctAnswer);
                        };

                        FlashCard flashCard = new FlashCard(question, answers, correctAnswerIndex);
                        flashCards.add(flashCard);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Fehlerbehandlung
        }

        return flashCards;
    }
}
