package tasktracker.service;

import tasktracker.model.Task;
import tasktracker.model.Person;
import tasktracker.model.Status;
import tasktracker.repository.ITaskRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TaskService {
    private final ITaskRepository repository;
    
    public TaskService(ITaskRepository repository) {
        this.repository = repository;
    }
    
    // ========== MAIN METHODS ==========
    
    public Task createTask(String title, String description, Person assignee) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Name of a task can't be empty");
        }
        
        Task task = new Task(title, description, assignee);
        return repository.save(task);
    }
    
    public Optional<Task> findTaskById(UUID id) {
        return repository.findById(id);
    }
    
    public List<Task> getAllTasks() {
        return repository.findAll();
    }
    
    public Task updateTaskStatus(UUID id, Status newStatus) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("The task is not found"));
        
        task.setStatus(newStatus);
        repository.save(task);
        return task;
    }
    
    public Task assignTask(UUID id, Person assignee) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("The task is not found"));
        
        if (assignee != null) {
            repository.savePerson(assignee);
        }
        task.setAssignee(assignee);
        repository.save(task);
        return task;
    }
    
    public void deleteTask(UUID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("The task is not found");
        }
        repository.deleteById(id);
    }
    
    // ========== METHODS FOR FILTERS ==========
    
    public List<Task> getTasksByStatus(Status status) {
        return repository.findByStatus(status);
    }
    
    public List<Task> getTasksByAssignee(Person assignee) {
        return repository.findByAssignee(assignee);
    }
    
    public List<Task> searchTasks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll();
        }
        
        String lowerKeyword = keyword.toLowerCase().trim();
        return repository.findAll().stream()
            .filter(task -> {
                boolean titleMatches = task.getTitle() != null && 
                    task.getTitle().toLowerCase().contains(lowerKeyword);
                
                boolean descMatches = task.getDescription() != null && 
                    task.getDescription().toLowerCase().contains(lowerKeyword);
                
                return titleMatches || descMatches;
            })
            .toList();
    }
    
    // ========== WORK WITH PERSONS ==========
    
    public Person findOrCreatePerson(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name can't be empty.");
        }
        
        return repository.findPersonByName(name.trim())
                .orElseGet(() -> {
                    Person newPerson = new Person(name.trim());
                    repository.savePerson(newPerson);
                    return newPerson;
                });
    }
    
    public long getTaskCount() {
        return repository.count();
    }
    
    public void deleteAllTasks() {
        repository.deleteAll();
    }
}