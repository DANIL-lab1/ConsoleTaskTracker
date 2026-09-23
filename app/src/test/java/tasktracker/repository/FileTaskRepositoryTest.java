package tasktracker.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasktracker.model.Task;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class FileTaskRepositoryTest {
    private static final String TEST_FILE = "test-tasks.json";
    private FileTaskRepository repository;
    
    @BeforeEach
    void setUp() {
        new File(TEST_FILE).delete();
        repository = new FileTaskRepository(TEST_FILE);
    }
    
    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }
    
    @Test
    @DisplayName("Save task persists to file")
    void testSave_PersistsTask() {
        Task task = new Task("Test", "Desc", null);
        repository.save(task);
        
        assertTrue(new File(TEST_FILE).exists());
    }
    
    @Test
    @DisplayName("Load tasks from file")
    void testLoad_LoadsTasks() {
        Task task = new Task("Test", "Desc", null);
        repository.save(task);
        
        FileTaskRepository newRepository = new FileTaskRepository(TEST_FILE);
        
        assertEquals(1, newRepository.count());
    }
    
    @Test
    @DisplayName("Delete task persists change")
    void testDelete_PersistsChange() {
        Task task = new Task("Test", "Desc", null);
        repository.save(task);
        repository.deleteById(task.getId());
        
        FileTaskRepository newRepository = new FileTaskRepository(TEST_FILE);
        
        assertEquals(0, newRepository.count());
    }
}
