package main;

import handler.FlashCardHandler;
import handler.GameHandler;
import data.InMemoryDatabase;
import handler.PlayerHandler;
import java.net.InetSocketAddress;

/**
 * Die Hauptklasse der Anwendung, die verschiedene Handler initialisiert und den WebSocket-Server startet.
 * <p>
 * Diese Klasse initialisiert die benötigten Datenbanken und Handler für das Spiel und startet einen WebSocket-Server,
 * der auf einem angegebenen Port lauscht.
 * </p>
 */
public class App {
    // Data
    public InMemoryDatabase inMemoryDatabase = new InMemoryDatabase();

    //Handler
    public PlayerHandler playerHandler = new PlayerHandler(this.inMemoryDatabase);
    public FlashCardHandler flashCardHandler = new FlashCardHandler(this.inMemoryDatabase);
    public GameHandler gameHandler = new GameHandler(this.flashCardHandler, this.playerHandler);

    /**
     * Der Einstiegspunkt der Anwendung. Initialisiert die App, konfiguriert den Server und startet ihn.
     *
     * @param args die Kommandozeilenargumente
     */
    public static void main(String[] args) {
        // Init main.App
        App app = new App();
        // Server Configuration
        int port = 3030; // Portnummer anpassen, falls nötig
        var server = new Controller(new InetSocketAddress(port), app);
        server.start();
        System.out.println("WebSocket Server gestartet auf Port " + port);
    }
}
