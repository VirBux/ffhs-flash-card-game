package handler;

import com.fasterxml.jackson.databind.JsonNode;
import model.Message;
import util.Json;

/**
 * Der ClientMessageHandler verarbeitet eingehende Nachrichten vom Client und extrahiert die relevanten Daten.
 *
 * @param <T> der Typ des Nachrichtinhalts
 */
public class ClientMessageHandler<T> extends Message<T> {
    /**
     * Konstruktor, der eine Nachricht als String empfängt und in ein JsonNode-Objekt parst.
     *
     * @param message die empfangene Nachricht als String
     */
    public ClientMessageHandler(String message) {
        try{
            JsonNode messageJson = Json.parse(message);
            this.commandName = messageJson.get("commandName").asText();
            this.body = (T) messageJson.get("body");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gibt den Namen des Befehls zurück, der in der Nachricht enthalten ist.
     *
     * @return der Befehlsname als String
     */
    public String getCommandName() {
        return this.commandName;
    }
}
