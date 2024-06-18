package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Die Json-Klasse bietet Hilfsmethoden zum Parsen und Serialisieren von JSON-Daten.
 */
public class Json {
    private static ObjectMapper objectMapper = getDefaultObjectMapper();

    /**
     * Erstellt und konfiguriert einen Standard-ObjectMapper.
     *
     * @return der konfigurierte ObjectMapper
     */
    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        return defaultObjectMapper;
    }

    /**
     * Parst einen JSON-String und gibt das entsprechende JsonNode-Objekt zurück.
     *
     * @param src der JSON-String
     * @return das geparste JsonNode-Objekt
     * @throws IOException wenn ein Fehler beim Parsen auftritt
     */
    public static JsonNode parse(String src) throws IOException {
        return objectMapper.readTree(src);
    }

    /**
     * Serialisiert ein Objekt in einen JSON-String.
     *
     * @param value das zu serialisierende Objekt
     * @return der JSON-String
     * @throws JsonProcessingException wenn ein Fehler bei der Serialisierung auftritt
     */
    public static String toJson(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}
