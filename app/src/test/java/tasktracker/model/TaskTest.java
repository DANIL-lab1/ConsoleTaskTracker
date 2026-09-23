package tasktracker.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    
    @Test
    @DisplayName("New task has NEW status and MEDUIM priority")
    void testNewTas_DefaultValues(){
        Task task = new Task("Test", "Desc", null);
        
        assertEquals(Status.NEW, task.getStatus());
        assertEquals(Priority.MEDIUM, task.getPriority());
        assertNull(task.getDeadline());
        assertNull(task.getAssignee());
        assertNotNull(task.getId());
        assertNotNull(task.getCreatedAt());
    }
    
    @Test
    @DisplayName("Task with past deadline is overdue")
    void testIsOverdue_PastDeadline_ReturnsTrue() {
        Task task = new Task("Test", "Desc", null);
        task.setDeadline(LocalDateTime.now().minusDays(1));
        
        assertTrue(task.isOverdue());
    }
    
    @Test
    @DisplayName("Task with future deadline is not overdue")
    void testIsOverdue_FutureDeadline_ReturnsFalse() {
        Task task = new Task("Test", "Desc", null);
        task.setDeadline(LocalDateTime.now().plusDays(1));
        
        assertFalse(task.isOverdue());
    }
    
    @Test
    @DisplayName("Task without deadline is not overdue")
    void testIsOverdue_NullDeadline_ReturnsFalse() {
        Task task = new Task("Test", "Desc", null);
        
        assertFalse(task.isOverdue());
    }
    
    @Test
    @DisplayName("Two tasks with same id are equal")
    void testEquals_SameId_ReturnsTrue() {
        Task task1 = new Task("Test", "Desc", null);
        Task task2 = new Task(
            task1.getId(), "Other", "Other", Status.DONE,
            Priority.HIGH, null, null,
            task1.getCreatedAt(), task1.getUpdatedAt()
        );
        
        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
    }
    
    @Test
    @DisplayName("Two tasks with different ids are not equal")
    void testEquals_DifferentId_ReturnsFalse() {
        Task task1 = new Task("Test", "Desc", null);
        Task task2 = new Task("Test", "Desc", null);
        
        assertNotEquals(task1, task2);
    }
    
    @Test
    @DisplayName("Setters update values")
    void testSetters_UpdateValues() {
        Task task = new Task("Test", "Desc", null);

        task.setTitle("New Title");
        task.setDescription("New Desc");
        task.setPriority(Priority.HIGH);

        assertEquals("New Title", task.getTitle());
        assertEquals("New Desc", task.getDescription());
        assertEquals(Priority.HIGH, task.getPriority());
    }

    @Test
    @DisplayName("toString contains title and status")
    void testToString() {
        Task task = new Task("Test", "Desc", null);
        String str = task.toString();

        assertTrue(str.contains("Test"));
        assertTrue(str.contains("NEW"));
    }
}
