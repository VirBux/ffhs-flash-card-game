package handler;

import data.InMemoryDatabase;
import model.Player;
import org.java_websocket.WebSocket;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Der PlayerHandler verwaltet die Spieler im Spiel, einschliesslich deren Hinzufügung, Entfernung und Statusverwaltung.
 */
public class PlayerHandler {
    private InMemoryDatabase inMemoryDatabase;

    public PlayerHandler(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    public Set<Player> addPlayer(Player player, WebSocket conn) {
        System.out.println("Spieler " + player.getUserName() + " ist beigetreten!");
        this.inMemoryDatabase.addPlayer(player);
        return this.inMemoryDatabase.getAllPlayers();
    }

    public Set<Player> getAllPlayersFromDb() {
        return this.inMemoryDatabase.getAllPlayers();
    }

    public Player removePlayer(Set<WebSocket> connections,  WebSocket conn) {
        Player playerToRemove = findPlayerByConnection(inMemoryDatabase.getAllPlayers(), conn);

        if (playerToRemove != null) {
            System.out.println("Spieler " + playerToRemove.getUserName() + " hat das Spiel verlassen");
            connections.remove(conn);
            inMemoryDatabase.removePlayer(playerToRemove);
            return playerToRemove;
        }

        return null;
    }

    public void setAllToPlaying() {
        var currentPlayers = inMemoryDatabase.getAllPlayers();
        currentPlayers.forEach(player -> player.setPlaying(true));
    }

    public void unsetAllPlaying() {
        var currentPlayers = inMemoryDatabase.getAllPlayers();
        currentPlayers.forEach(player -> player.setPlaying(false));
    }

    public static Player findPlayerByConnection(Set<Player> players, WebSocket connection) {
        for (Player player : players) {
            if (player.hasConnection(connection)) return player;
        }
        return null;
    }

    public Player findPlayerByUuid(UUID uuid) {
        var allPlayers = this.inMemoryDatabase.getAllPlayers();
        for (Player player : allPlayers) {
            if(player.getUuid().equals(uuid)) return player;
        }
        return null;
    }

    public Set<WebSocket> getAllConnection() {
        var allPlayers = this.inMemoryDatabase.getAllPlayers();
        return allPlayers.stream()
                .map(Player::getConnection)
                .collect(Collectors.toSet());
    }

    public Set<WebSocket> getAllPlayingConnection() {
        var allPlayers = this.inMemoryDatabase.getAllPlayers();
        return allPlayers.stream()
                .filter(Player::isPlaying)
                .map(Player::getConnection)
                .collect(Collectors.toSet());
    }

    public void resetAllPoints() {
        var allPlayers = this.inMemoryDatabase.getAllPlayers();
        allPlayers.forEach(Player::resetPoints);
    }
}
