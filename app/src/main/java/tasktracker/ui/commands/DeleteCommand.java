package tasktracker.ui.commands;

import tasktracker.service.TaskService;
import java.util.UUID;
import tasktracker.util.ColorUtils;

public class DeleteCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        if (args.length < 2) {
            System.out.println("ERROR: Task ID required.");
            System.out.println("Usage: " + getUsage());
            return;
        }
        
        try {
            UUID id = UUID.fromString(args[1]);
            service.deleteTask(id);
            ColorUtils.printSuccess("Task deleted!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR: Invalid ID format.");
        }
    }
    
    @Override
    public String getDescription() {
        return "Delete a task";
    }
    
    @Override
    public String getUsage() {
        return "delete <id>";
    }
    
    @Override
    public String getCategory() {
        return "TASK MANAGEMENT";
    }
}