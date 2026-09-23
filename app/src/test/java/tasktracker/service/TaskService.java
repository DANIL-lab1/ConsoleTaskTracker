package tasktracker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasktracker.model.Task;
import tasktracker.model.Person;
import tasktracker.model.Status;
import tasktracker.repository.ITaskRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TaskService {
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    
    private final ITaskRepository repository;
    private Task lastDeletedTask;
    
    public TaskService(ITaskRepository repository) {
        this.repository = repository;
    }
    
    // ========== MAIN METHODS ==========
    
    public Task createTask(String title, String description, Person assignee) {
        if (title == null || title.trim().isEmpty()) {
            log.warn("Attempt to create task with empty title");
            throw new IllegalArgumentException("Name of a task can't be empty");
        }
        Task task = new Task(title, description, assignee);
        Task saved = repository.save(task);
        log.info("Task created: {} (id={})", title, saved.getId());
        return saved;
    }
    
    public Optional<Task> findTaskById(UUID id) {
        Optional<Task> task = repository.findById(id);
        if (task.isEmpty()) {
            log.warn("Task not found: {}", id);
        }
        return task;
    }
    
    public Optional<Task> findTaskByIdOrPrefix(String idOrPrefix) {
        if (idOrPrefix == null || idOrPrefix.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            UUID id = UUID.fromString(idOrPrefix);
            return repository.findById(id);
        } catch (IllegalArgumentException e) {
            String prefix = idOrPrefix.toLowerCase().trim();

            if (prefix.length() < 4) {
                throw new IllegalArgumentException("ID prefix must be at least 4 characters");
            }

            List<Task> matches = repository.findAll().stream()
                .filter(task -> task.getId().toString().toLowerCase().startsWith(prefix))
                .toList();

            if (matches.isEmpty()) {
                return Optional.empty();
            }

            if (matches.size() > 1) {
                throw new IllegalArgumentException(
                    "Ambiguous ID prefix '" + prefix + "'. Found " + matches.size() + " tasks. Use more characters."
                );
            }

            return Optional.of(matches.get(0));
        }
    }
    
    public List<Task> getAllTasks() {
        return repository.findAll();
    }
    
    public Task updateTaskStatus(UUID id, Status newStatus) {
        Task task = repository.findById(id)
            .orElseThrow(() -> {
                log.warn("Task not found: {}", id);
                return new IllegalArgumentException("The task is not found");
            });

        task.setStatus(newStatus);
        repository.save(task);
        log.info("Status updated: {} -> {}", task.getTitle(), newStatus);
        return task;
    }
    
    public Task updateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        return repository.save(task);
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
        Task task = repository.findById(id)
            .orElseThrow(() -> {
                log.warn("Task not found: {}", id);
                return new IllegalArgumentException("The task is not found");
            });

        lastDeletedTask = task;
        repository.deleteById(id);
        log.info("Task deleted: {} (id={})", task.getTitle(), id);
    }
    
    public Task restoreLastDeleted() {
        if (lastDeletedTask == null) {
            log.warn("Attempt to undo with no recently deleted task");
            throw new IllegalStateException("No recently deleted task to restore");
        }

        Task restored = lastDeletedTask;
        lastDeletedTask = null;
        Task saved = repository.save(restored);
        log.info("Task restored: {} (id={})", saved.getTitle(), saved.getId());
        return saved;
    }
    
    public boolean hasRecentlyDeleted() {
        return lastDeletedTask != null;
    }
    
    // ========== METHODS FOR FILTERS ==========
    
    public List<Task> getTasksByStatus(Status status) {
        return repository.findByStatus(status);
    }
    
    public List<Task> getTasksByAssignee(Person assignee) {
        return repository.findByAssignee(assignee);
    }
    
    public List<Task> getOverdueTasks() {
        return repository.findAll().stream()
            .filter(Task::isOverdue)
            .toList();
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