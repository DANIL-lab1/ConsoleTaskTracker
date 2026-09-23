package tasktracker.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentParserTest {
    
    @Test
    @DisplayName("Parse single key-value pair")
    void testParseArguments_SinglePair_ReturnsMap() {
        // Arrange
        String[] args = {"create", "--title", "Test"};
        
        // Act
        Map<String, String> result = ArgumentParser.parseArguments(args);
        
        // Assert
        assertEquals("Test", result.get("title"));
    }
    
    @Test
    @DisplayName("Parse quoted value with spaces")
    void testParseArguments_QuotedValue_ReturnsTrimmedValue() {
        // Arrange
        String[] args = {"create", "--title", "\"Test Task\""};
        
        // Act
        Map<String, String> result = ArgumentParser.parseArguments(args);
        
        // Assert
        assertEquals("Test Task", result.get("title"));
    }
    
    @Test
    @DisplayName("Parse flag without value")
    void testParseArguments_FlagWithoutValue_ContainsKey() {
        // Arrange
        String[] args = {"filter", "--overdue"};
        
        // Act
        Map<String, String> result = ArgumentParser.parseArguments(args);
        
        // Assert
        assertTrue(result.containsKey("overdue"));
    }
    
    @Test
    @DisplayName("Get value returns correct value")
    void testGetValue_ExistingKey_ReturnsValue() {
        // Arrange
        Map<String, String> args = Map.of("title", "Test");
        
        // Act
        String value = ArgumentParser.getValue(args, "title");
        
        // Assert
        assertEquals("Test", value);
    }
    
    @Test
    @DisplayName("Get value with default returns default when key missing")
    void testGetValue_MissingKey_ReturnsDefault() {
        // Arrange
        Map<String, String> args = Map.of();
        
        // Act
        String value = ArgumentParser.getValue(args, "missing", "default");
        
        // Assert
        assertEquals("default", value);
    }
    
    @Test
    @DisplayName("Has flag returns true or false correctly")
    void testHasFlag_ChecksCorrectly() {
        // Arrange
        Map<String, String> args = Map.of("overdue", "");
        
        // Assert
        assertTrue(ArgumentParser.hasFlag(args, "overdue"));
        assertFalse(ArgumentParser.hasFlag(args, "status"));
    }
}