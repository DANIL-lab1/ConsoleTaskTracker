package tasktracker.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasktracker.model.Person;
import tasktracker.model.Status;
import tasktracker.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskRepositoryTest {
    
    private InMemoryTaskRepository repository;
    
    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();
    }
    
    @Test
    @DisplayName("Save and find task by id")
    void testSaveAndFindById() {
        Task task = new Task("Test", "Desc", null);
        repository.save(task);
        
        assertTrue(repository.findById(task.getId()).isPresent());
        assertEquals(task, repository.findById(task.getId()).get());
    }
    
    @Test
    @DisplayName("Find all tasks")
    void testFindAll(){
        repository.save(new Task("Task 1", "Desc", null));
        repository.save(new Task("Task 2", "Desc", null));
        
        List<Task> tasks = repository.findAll();
        assertEquals(2, tasks.size());
    }
    
    @Test
    @DisplayName("Find by status")
    void testFindByStatus(){
        Task task1 = new Task("Task 1", "Desc", null);
        Task task2 = new Task("Task 2", "Desc", null);
        task1.setStatus(Status.DONE);
        
        repository.save(task1);
        repository.save(task2);
        List<Task> done = repository.findByStatus(Status.DONE);
        assertEquals(1, done.size());
        assertEquals("Task 1", done.get(0).getTitle());
    }
    
    @Test
    @DisplayName("Delete task by id")
    void testDeleteById(){
        Task task = new Task("Test", "Desc", null);
        repository.save(task);
        
        repository.deleteById(task.getId());
        
        assertFalse(repository.existsById(task.getId()));
    }
    
    @Test
    @DisplayName("Save and find person by name")
    void testSavePersonAndFindByName() {
        Person person = new Person("Ivan");
        repository.savePerson(person);
        
        assertTrue(repository.findPersonByName("Ivan").isPresent());
        assertTrue(repository.findPersonByName("ivan").isPresent());
    }
    
    @Test
    @DisplayName("Count returns correct number")
    void testCount() {
        assertEquals(0, repository.count());
        
        repository.save(new Task("Task 1", "Desc", null));
        repository.save(new Task("Task 2", "Desc", null));
        
        assertEquals(2, repository.count());
    }
}
