package tasktracker.ui.commands;

import tasktracker.service.TaskService;
import java.util.UUID;

public class DeleteCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        if (args.length < 2) {
            System.out.println("  Error: Task ID required.");
            System.out.println("Usage: " + getUsage());
            return;
        }
        
        try {
            UUID id = UUID.fromString(args[1]);
            service.deleteTask(id);
            System.out.println("  Task deleted successfully!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("  Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Error: Invalid ID format.");
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
}