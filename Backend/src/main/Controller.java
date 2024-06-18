package main;

import com.fasterxml.jackson.databind.JsonNode;
import handler.ClientMessageHandler;
import model.*;
import handler.ServerMessageHandler;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Der Controller für den WebSocket-Server. Diese Klasse erweitert den {@link WebSocketServer}
 * und behandelt die Verbindungen, Nachrichten und Fehler des Servers.
 * <p>
 * Der Controller initialisiert und verwaltet die WebSocket-Verbindungen, leitet Nachrichten
 * an die entsprechenden Handler weiter und sendet Antworten an die Clients.
 * </p>
 */
public class Controller extends WebSocketServer {
    private App app;
    private final Set<WebSocket> connections = new HashSet<>();

    /**
     * @param address die Adresse, auf der der Server lauscht
     * @param app die Hauptanwendung, die die Handler und Datenbank enthält
     */
    public Controller(InetSocketAddress address, App app) {
        super(address);
        this.app = app;
    }

    /**
     * Wird aufgerufen, wenn eine neue Verbindung hergestellt wird.
     *
     * @param conn die WebSocket-Verbindung
     * @param handshake das Handshake-Objekt des Clients
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections.add(conn);
        var messageToClient = new ServerMessageHandler<ArrayList<FlashCard>>("flashCardsServer", this.app.flashCardHandler.getAllFlashCards());
        Controller.sendMessage(conn, messageToClient);
        System.out.println("Neue Verbindung: " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    /**
     * Wird aufgerufen, wenn eine Verbindung geschlossen wird.
     *
     * @param conn die WebSocket-Verbindung
     * @param code der Schliessungscode
     * @param reason der Schließungsgrund
     * @param remote gibt an, ob die Schliessung vom Client oder Server initiiert wurde
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        var removedPlayer = this.app.playerHandler.removePlayer(connections, conn);
        if (removedPlayer != null) {
            var messageToClient = new ServerMessageHandler<Player>("userLeftTheLobbyServer", removedPlayer);
            Controller.broadcastToAllOther(this.connections, conn, messageToClient);
            System.out.println("Verbindung geschlossen: " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
            this.app.gameHandler.resetGameIfNoPlayerArePlaying();
        } else {
            System.out.println("Verbindung geschlossen aber kein Spieler konnte zugeordnet werden: " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        }

    }

    /**
     * Wird aufgerufen, wenn eine Nachricht vom Client empfangen wird.
     *
     * @param conn die WebSocket-Verbindung
     * @param message die empfangene Nachricht
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message recived:");
        System.out.println(message);
        var clientMessage = new ClientMessageHandler<JsonNode>(message);

        switch(clientMessage.getCommandName()) {

            case "startGame":
                this.app.gameHandler.startGame(this.connections);
                break;

            case "addAnswer":
                this.app.gameHandler.playerAddAnswer(
                    new FlashCardPlayerAnswers(clientMessage)
                );
                break;

            case "addUser":
                var newPlayer = new Player(clientMessage, conn);
                Set<Player> players = this.app.playerHandler.addPlayer(
                    newPlayer,
                    conn
                );
                this.connections.add(conn);

                var playersMessageToClient = new ServerMessageHandler<ArrayList<Player>>(
                    "newUserAddedServer",
                        new ArrayList<Player>(players)
                );
                var newPlayerMessageToClient = new ServerMessageHandler<Player>(
                        "yourCreatedUserServer",
                        newPlayer
                );
                Controller.broadcastToAll(this.connections, playersMessageToClient);
                Controller.sendMessage(conn, newPlayerMessageToClient);
                Controller.broadcastToAll(
                        this.connections,
                        new ServerMessageHandler<Game>("serverCurrentGame", this.app.gameHandler.getCurrentGame())
                );
                break;

            case "getCurrentGame":
                var currentGame = this.app.gameHandler.getCurrentGame();
                var gameMessageToClient = new ServerMessageHandler<Game>("currentGameServer", currentGame);
                Controller.sendMessage(conn, gameMessageToClient);
                break;

            case "chatMessage":
                Controller.broadcastToAll(
                        this.connections,
                        new ServerMessageHandler<Chat>("serverChatMessage", new Chat(clientMessage))
                );
                break;

            default:
                String defaultMessage = "commandName doesnt match a pattern.";
                System.out.println(defaultMessage);
        }

        if ("##".equals(message)) {
            conn.close();
        }
    }

    /**
     * Wird aufgerufen, wenn ein Fehler auftritt.
     *
     * @param conn die WebSocket-Verbindung, auf der der Fehler auftrat (kann null sein)
     * @param ex die aufgetretene Ausnahme
     */
    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    /**
     * Wird aufgerufen, wenn der Server gestartet wird.
     */
    @Override
    public void onStart() {
        System.out.println("Server gestartet!");
    }

    /**
     * Sendet eine Nachricht an einen bestimmten Client.
     *
     * @param conn die WebSocket-Verbindung des Clients
     * @param message die Nachricht, die gesendet werden soll
     */
    public static void sendMessage(WebSocket conn, ServerMessageHandler<?> message){
        try {
            conn.send(message.getJsonString());
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Send message to a specific user:");
        System.out.println("Command Name: " + message.commandName);
        System.out.println(message.body);
    }

    /**
     * Sendet eine Nachricht an alle verbundenen Clients.
     *
     * @param connections die Menge der WebSocket-Verbindungen
     * @param message die Nachricht, die gesendet werden soll
     */
    public static void broadcastToAll(Set<WebSocket> connections, ServerMessageHandler<?> message) {
        for (WebSocket conn : connections) {
            try{
                if (conn.isOpen()) conn.send(message.getJsonString());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("Send message to all:");
        System.out.println("Command Name: " + message.commandName);
        System.out.println(message.body);
    }

    /**
     * Sendet eine Nachricht an alle verbundenen Clients ausser dem Absender.
     *
     * @param connections die Menge der WebSocket-Verbindungen
     * @param sender die WebSocket-Verbindung des Absenders
     * @param message die Nachricht, die gesendet werden soll
     */
    public static void broadcastToAllOther(Set<WebSocket> connections, WebSocket sender, ServerMessageHandler<?> message) {
        for (WebSocket conn : connections) {
            try{
                if (conn != sender && conn.isOpen()) conn.send(message.getJsonString());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("Send message to all, except for self:");
        System.out.println("Command Name: " + message.commandName);
        System.out.println(message.body);
    }
}
