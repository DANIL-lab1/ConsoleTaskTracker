package tasktracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasktracker.model.Person;
import tasktracker.model.Priority;
import tasktracker.model.Status;
import tasktracker.model.Task;
import tasktracker.repository.InMemoryTaskRepository;
import tasktracker.repository.ITaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    
    private TaskService service;
    private ITaskRepository repository;
    
    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();
        service = new TaskService(repository);
    }
    
    // ========== createTask ==========
    
    @Test
    @DisplayName("Create task with valid title returns task")
    void testCreateTask_ValidTitle_ReturnsTask() {
        // Arrange
        String title = "Learn Java";
        String description = "Read book";
        
        // Act
        Task task = service.createTask(title, description, null);
        
        // Assert
        assertNotNull(task);
        assertEquals(title, task.getTitle());
        assertEquals(description, task.getDescription());
        assertEquals(Status.NEW, task.getStatus());
    }
    
    @Test
    @DisplayName("Create task with empty title throws exception")
    void testCreateTask_EmptyTitle_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.createTask("", "Description", null);
        });
    }
    
    @Test
    @DisplayName("Create task with null title throws exception")
    void testCreateTask_NullTitle_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.createTask(null, "Description", null);
        });
    }
    
    // ========== updateTaskStatus ==========
    
    @Test
    @DisplayName("Update status of existing task works")
    void testUpdateTaskStatus_ExistingTask_UpdatesStatus() {
        // Arrange
        Task task = service.createTask("Test", "Desc", null);
        
        // Act
        Task updated = service.updateTaskStatus(task.getId(), Status.DONE);
        
        // Assert
        assertEquals(Status.DONE, updated.getStatus());
    }
    
    @Test
    @DisplayName("Update status of non-existing task throws exception")
    void testUpdateTaskStatus_NonExistingTask_ThrowsException() {
        // Arrange
        UUID randomId = UUID.randomUUID();
        
        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.updateTaskStatus(randomId, Status.DONE);
        });
    }
    
    // ========== deleteTask ==========
    
    @Test
    @DisplayName("Delete existing task works")
    void testDeleteTask_ExistingTask_DeletesTask() {
        // Arrange
        Task task = service.createTask("Test", "Desc", null);
        long countBefore = service.getTaskCount();
        
        // Act
        service.deleteTask(task.getId());
        
        // Assert
        assertEquals(countBefore - 1, service.getTaskCount());
    }
    
    @Test
    @DisplayName("Delete non-existing task throws exception")
    void testDeleteTask_NonExistingTask_ThrowsException() {
        // Arrange
        UUID randomId = UUID.randomUUID();
        
        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.deleteTask(randomId);
        });
    }
    
    // ========== undo ==========
    
    @Test
    @DisplayName("Restore last deleted task works")
    void testRestoreLastDeleted_AfterDelete_RestoresTask() {
        // Arrange
        Task task = service.createTask("Test", "Desc", null);
        service.deleteTask(task.getId());
        
        // Act
        Task restored = service.restoreLastDeleted();
        
        // Assert
        assertNotNull(restored);
        assertEquals(task.getTitle(), restored.getTitle());
    }
    
    @Test
    @DisplayName("Restore when nothing deleted throws exception")
    void testRestoreLastDeleted_NothingDeleted_ThrowsException() {
        assertThrows(IllegalStateException.class, () -> {
            service.restoreLastDeleted();
        });
    }
    
    // ========== searchTasks ==========
    
    @Test
    @DisplayName("Search by keyword finds matching tasks")
    void testSearchTasks_MatchingKeyword_ReturnsTasks() {
        // Arrange
        service.createTask("Learn Java", "Read book", null);
        service.createTask("Learn Python", "Read book", null);
        service.createTask("Buy groceries", "Milk, bread", null);
        
        // Act
        List<Task> results = service.searchTasks("Java");
        
        // Assert
        assertEquals(1, results.size());
        assertEquals("Learn Java", results.get(0).getTitle());
    }
    
    @Test
    @DisplayName("Search with empty keyword returns all tasks")
    void testSearchTasks_EmptyKeyword_ReturnsAllTasks() {
        // Arrange
        service.createTask("Task 1", "Desc", null);
        service.createTask("Task 2", "Desc", null);
        
        // Act
        List<Task> results = service.searchTasks("");
        
        // Assert
        assertEquals(2, results.size());
    }
    
    // ========== findOrCreatePerson ==========
    
    @Test
    @DisplayName("Find or create person creates new person")
    void testFindOrCreatePerson_NewPerson_CreatesPerson() {
        // Act
        Person person = service.findOrCreatePerson("Ivan");
        
        // Assert
        assertNotNull(person);
        assertEquals("Ivan", person.getName());
    }
    
    @Test
    @DisplayName("Find or create person returns existing person")
    void testFindOrCreatePerson_ExistingPerson_ReturnsSame() {
        // Arrange
        Person first = service.findOrCreatePerson("Ivan");
        
        // Act
        Person second = service.findOrCreatePerson("Ivan");
        
        // Assert
        assertEquals(first.getId(), second.getId());
    }
    
    @Test
    @DisplayName("Find or create person with empty name throws exception")
    void testFindOrCreatePerson_EmptyName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.findOrCreatePerson("");
        });
    }
    
    @Test
    @DisplayName("Update task saves changes")
    void testUpdateTask_SavesChanges() {
        Task task = service.createTask("Test", "Desc", null);
        task.setPriority(Priority.HIGH);

        Task updated = service.updateTask(task);

        assertEquals(Priority.HIGH, updated.getPriority());
    }

    @Test
    @DisplayName("Get tasks by assignee returns matching tasks")
    void testGetTasksByAssignee() {
        Person ivan = service.findOrCreatePerson("Ivan");
        Person anna = service.findOrCreatePerson("Anna");

        service.createTask("Task 1", "Desc", ivan);
        service.createTask("Task 2", "Desc", ivan);
        service.createTask("Task 3", "Desc", anna);

        List<Task> ivanTasks = service.getTasksByAssignee(ivan);

        assertEquals(2, ivanTasks.size());
    }

    @Test
    @DisplayName("Get overdue tasks returns only overdue")
    void testGetOverdueTasks_ReturnsOnlyOverdue() {
        Task overdue = service.createTask("Overdue", "Desc", null);
        overdue.setDeadline(LocalDateTime.now().minusDays(1));
        service.updateTask(overdue);

        Task future = service.createTask("Future", "Desc", null);
        future.setDeadline(LocalDateTime.now().plusDays(1));
        service.updateTask(future);

        List<Task> overdueTasks = service.getOverdueTasks();

        assertEquals(1, overdueTasks.size());
    }

    @Test
    @DisplayName("Delete all tasks clears repository")
    void testDeleteAllTasks() {
        service.createTask("Task 1", "Desc", null);
        service.createTask("Task 2", "Desc", null);

        service.deleteAllTasks();

        assertEquals(0, service.getTaskCount());
    }

    @Test
    @DisplayName("hasRecentlyDeleted returns true after delete")
    void testHasRecentlyDeleted_AfterDelete() {
        Task task = service.createTask("Test", "Desc", null);
        service.deleteTask(task.getId());

        assertTrue(service.hasRecentlyDeleted());
    }
    
    @Test
    @DisplayName("Find task by prefix returns matching task")
    void testFindTaskByIdOrPrefix_FullId_ReturnsTask() {
        Task task = service.createTask("Test", "Desc", null);

        Optional<Task> found = service.findTaskByIdOrPrefix(task.getId().toString());

        assertTrue(found.isPresent());
        assertEquals(task.getId(), found.get().getId());
    }

    @Test
    @DisplayName("Get tasks by status returns matching")
    void testGetTasksByStatus() {
        Task task1 = service.createTask("Task 1", "Desc", null);
        Task task2 = service.createTask("Task 2", "Desc", null);
        service.updateTaskStatus(task1.getId(), Status.DONE);

        List<Task> done = service.getTasksByStatus(Status.DONE);

        assertEquals(1, done.size());
    }

    @Test
    @DisplayName("Get task count returns correct number")
    void testGetTaskCount() {
        service.createTask("Task 1", "Desc", null);
        service.createTask("Task 2", "Desc", null);

        assertEquals(2, service.getTaskCount());
    }
}
