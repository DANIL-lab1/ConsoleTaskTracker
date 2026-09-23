package tasktracker.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    
    @Test
    @DisplayName("New person has name and id")
    void testNewPerson() {
        Person person = new Person("Ivan");
        
        assertNotNull(person.getId());
        assertEquals("Ivan", person.getName());
    }
    
    @Test
    @DisplayName("Person with same id is equal")
    void testEquals_SameId() {
        Person person1 = new Person("Ivan");
        Person person2 = new Person(person1.getId(), "Ivan");
        
        assertEquals(person1, person2);
        assertEquals(person1.hashCode(), person2.hashCode());
    }
    
    @Test
    @DisplayName("Person with different id is not equal")
    void testEquals_DifferentId() {
        Person person1 = new Person("Ivan");
        Person person2 = new Person("Ivan");
        
        assertNotEquals(person1, person2);
    }
    
    @Test
    @DisplayName("toString returns name")
    void testToString() {
        Person person = new Person("Ivan");
        
        assertEquals("Ivan", person.toString());
    }
    
    @Test
    @DisplayName("Set name updates name")
    void testSetName() {
        Person person = new Person("Ivan");
        person.setName("Anna");

        assertEquals("Anna", person.getName());
    }

    @Test
    @DisplayName("Get id returns non-null")
    void testGetId() {
        Person person = new Person("Ivan");

        assertNotNull(person.getId());
    }
}
