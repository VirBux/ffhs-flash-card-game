package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import handler.ClientMessageHandler;
import org.java_websocket.WebSocket;

import java.util.UUID;

public class Player implements Comparable<Player> {
    @JsonIgnore
    private WebSocket connection;
    private UUID uuid = UUID.randomUUID();
    private String userName;
    private int points;
    private boolean isPlaying = false;

    public Player(String userName, WebSocket connection) {
        this.userName = userName;
        this.connection = connection;
    }

    public Player(ClientMessageHandler<JsonNode> clientMessage, WebSocket connection) {
        this.userName = clientMessage.body.get("name").asText();
        this.connection = connection;
    }

    // Getter
    public UUID getUuid() {
        return uuid;
    }

    public String getUserName() {
        return userName;
    }

    public int getPoints() {
        return points;
    }

    public WebSocket getConnection() {
        return this.connection;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    // Setter
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void addPoints(int newPoints) {
        this.points += newPoints;
    }

    public void resetPoints() {
        this.points = 0;
    }

    public boolean hasConnection(WebSocket connection) {
        return this.connection.equals(connection);
    }

    @Override
    public int compareTo(Player other) {
        return this.uuid.compareTo(other.uuid);
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    @Override
    public String toString() {
        return "Game{" +
                "uuid=" + uuid +
                ", userName='" + userName + '\'' +
                ", points=" + points +
                ", isPlaying=" + isPlaying +
                '}';
    }
}
