package tasktracker.app;

import tasktracker.repository.InMemoryTaskRepository;
import tasktracker.repository.ITaskRepository;
import tasktracker.service.TaskService;
import tasktracker.ui.ConsoleApp;

public class Main {
    public static void main(String[] args) {
        ITaskRepository repository = new InMemoryTaskRepository();
        TaskService service = new TaskService(repository);
        
        ConsoleApp app = new ConsoleApp(service);
        app.start();
    }
}