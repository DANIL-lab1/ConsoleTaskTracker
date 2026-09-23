package tasktracker.repository;

import tasktracker.model.Task;
import tasktracker.model.Person;
import tasktracker.model.Status;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryTaskRepository implements ITaskRepository {
    private final Map<UUID, Task> tasks = new ConcurrentHashMap<>();
    private final Map<String, Person> persons = new ConcurrentHashMap<>();
    
    @Override
    public Task save(Task task){
        tasks.put(task.getId(), task);
        if (task.getAssignee() != null) {
            savePerson(task.getAssignee());
        }
        return task;
    }
    
    @Override
    public Optional<Task> findById(UUID id){
        return Optional.ofNullable(tasks.get(id));
    }
    
    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }
    
    @Override
    public List<Task> findByStatus(Status status){
        return tasks.values().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Task> findByAssignee(Person person){
        return tasks.values().stream()
                .filter(task -> person.equals(task.getAssignee()))
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(UUID id) {
        tasks.remove(id);
    }
    
    @Override
    public void deleteAll() {
        tasks.clear();
    }
    
    @Override
    public boolean existsById(UUID id) {
        return tasks.containsKey(id);
    }
    
    @Override
    public long count() {
        return tasks.size();
    }
    
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
