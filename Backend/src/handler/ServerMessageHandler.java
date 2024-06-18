package handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.Message;
import org.java_websocket.WebSocket;
import util.Json;

import java.util.Set;

/**
 * Der ServerMessageHandler verarbeitet und formatiert Nachrichten, die vom Server an die Clients gesendet werden.
 *
 * @param <T> der Typ des Nachrichtinhalts
 */
public class ServerMessageHandler<T> extends Message<T> {

    /**
     * @param commandName der Name des Befehls, der in der Nachricht enthalten ist
     * @param object der Inhalt der Nachricht
     */
    public ServerMessageHandler(String commandName, T object) {
            this.commandName = commandName;
            this.body = object;
    }

    /**
     * Gibt die Nachricht als JSON-String zurück.
     *
     * @return die Nachricht als JSON-String
     * @throws JsonProcessingException wenn die Nachricht nicht in JSON umgewandelt werden kann
     */
    public String getJsonString() throws JsonProcessingException {
        String bodyString = Json.toJson(this.body);
        return "{\"commandName\": \""+ this.commandName + "\"," + "\"body\":" + bodyString + "}";
    }
}
