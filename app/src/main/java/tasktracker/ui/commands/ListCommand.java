package tasktracker.ui.commands;

import tasktracker.model.Task;
import tasktracker.service.TaskService;
import tasktracker.ui.TablePrinter;
import java.util.List;

public class ListCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        List<Task> tasks = service.getAllTasks();
        
        if (tasks.isEmpty()) {
            System.out.println("INFO: No tasks found.");
            return;
        }
        
        System.out.println("All tasks (" + tasks.size() + "):");
        TablePrinter.printTasks(tasks);
    }
    
    @Override
    public String getDescription() {
        return "List all tasks";
    }
    
    @Override
    public String getUsage() {
        return "list";
    }
    
    @Override
    public String getCategory() {
        return "TASK MANAGEMENT";
    }
}