package model;

import mock.WebSocketMock;
import org.junit.Before;
import org.junit.Test;
import util.Json;

import static org.junit.Assert.*;

import java.util.UUID;

public class PlayerTest {
    private Player player;
    private UUID uuid;
    private String initialUserName = "JohnDoe";
    private int initialPoints = 0;
    private WebSocketMock webSocketMock;

    //@Before
    public void setUp() {
        webSocketMock = new WebSocketMock();
        player = new Player(initialUserName, webSocketMock);
        uuid = player.getUuid();
    }

    @Test
    public void testGetUuid() {
        assertEquals("UUID should match the one set in constructor", uuid, player.getUuid());
    }

    @Test
    public void testGetUserName() {
        assertEquals("Username should match the one set in constructor", initialUserName, player.getUserName());
    }

    @Test
    public void testGetPoints() {
        assertEquals("Initial points should be zero", initialPoints, player.getPoints());
    }

    @Test
    public void testSetUserName() {
        String newUserName = "JaneDoe";
        player.setUserName(newUserName);
        assertEquals("Username should be updated to new username", newUserName, player.getUserName());
    }

    @Test
    public void testAddPoints() {
        int pointsToAdd = 10;
        player.addPoints(pointsToAdd);
        assertEquals("Points should be increased by the amount added", initialPoints + pointsToAdd, player.getPoints());
    }

    @Test
    public void testPlayerToJson() throws Exception {
        String jsonResult = Json.toJson(player);
        assertNotNull("The JSON output should not be null", jsonResult);
        assertTrue("JSON should contain the UUID", jsonResult.contains(uuid.toString()));
        assertTrue("JSON should contain the username", jsonResult.contains(initialUserName));
        assertTrue("JSON should contain the initial points as 0", jsonResult.contains("\"points\":0"));
    }
}