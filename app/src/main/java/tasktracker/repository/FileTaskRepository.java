package tasktracker.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import tasktracker.model.Person;
import tasktracker.model.Status;
import tasktracker.model.Task;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FileTaskRepository implements ITaskRepository{
    
    private final Map<UUID, Task> tasks = new ConcurrentHashMap<>();
    private final Map<String, Person> persons = new ConcurrentHashMap<>();
    private final File file;
    private final ObjectMapper mapper;
    
    public FileTaskRepository(String filePath) {
        this.file = new File(filePath);
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        load();
    }
    
    // ========== PERSISTENCE ==========
    
    private void load() {
        if (!file.exists()) {
            System.out.println("INFO: No save file found. Starting fresh.");
            return;
        }
        
        try {
            List<Task> loaded = mapper.readValue(file, new TypeReference<List<Task>>() {});
            for (Task task : loaded) {
                tasks.put(task.getId(), task);
                if (task.getAssignee() != null) {
                    persons.put(task.getAssignee().getName().toLowerCase(), task.getAssignee());
                }
            }
            System.out.println("INFO: Loaded " + loaded.size() + " tasks from " + file.getName());
        } catch (IOException e) {
            System.err.println("ERROR loading tasks: " + e.getMessage());
        }
    }
    
    private void saveToFile() {
        try {
            List<Task> allTasks = new ArrayList<>(tasks.values());
            mapper.writeValue(file, allTasks);
        } catch (IOException e) {
            System.err.println("ERROR saving tasks: " + e.getMessage());
        }
    }
    
    // ========== TASK METHODS ==========
    
    @Override
    public Task save(Task task) {
        tasks.put(task.getId(), task);
        if (task.getAssignee() != null) {
            savePerson(task.getAssignee());
        }
        saveToFile();
        return task;
    }
    
    @Override
    public Optional<Task> findById(UUID id) {
        return Optional.ofNullable(tasks.get(id));
    }
    
    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }
    
    @Override
    public List<Task> findByStatus(Status status) {
        return tasks.values().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Task> findByAssignee(Person person) {
        return tasks.values().stream()
                .filter(task -> person.equals(task.getAssignee()))
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(UUID id) {
        tasks.remove(id);
        saveToFile();
    }
    
    @Override
    public void deleteAll() {
        tasks.clear();
        saveToFile();
    }
    
    @Override
    public boolean existsById(UUID id) {
        return tasks.containsKey(id);
    }
    
    @Override
    public long count() {
        return tasks.size();
    }
    
    // ========== PERSON METHODS ==========
    
    @Override
    public Person savePerson(Person person) {
        persons.put(person.getName().toLowerCase(), person);
        return person;
    }
    
    @Override
    public Optional<Person> findPersonByName(String name) {
        return Optional.ofNullable(persons.get(name.toLowerCase()));
    }
    
    @Override
    public List<Person> findAllPersons() {
        return new ArrayList<>(persons.values());
    }
}
