package model;

import com.fasterxml.jackson.databind.JsonNode;
import handler.ClientMessageHandler;

public class Chat {
    public String userName;
    public String message;

    public Chat(ClientMessageHandler<JsonNode> clientMessageHandler) {
        this.userName = clientMessageHandler.body.get("userName").asText();
        this.message = clientMessageHandler.body.get("message").asText();
    }

    @Override
    public String toString() {
        return "Chat{" +
                "userName=" + userName +
                ", message='" + message + '\'' +
                '}';
    }
}
