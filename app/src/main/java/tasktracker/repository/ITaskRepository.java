package tasktracker.repository;

import tasktracker.model.Task;
import tasktracker.model.Person;
import tasktracker.model.Status;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITaskRepository {
    // Базовые CRUD методы
    Task save(Task task);
    Optional<Task> findById(UUID id);
    List<Task> findAll();
    List<Task> findByStatus(Status status);
    List<Task> findByAssignee(Person person);
    void deleteById(UUID id);
    void deleteAll();
    boolean existsById(UUID id);
    long count();
    
    // Метод для работы с исполнителем
    Person savePerson(Person person);
    Optional<Person> findPersonByName(String name);
    List<Person> findAllPersons();
}
