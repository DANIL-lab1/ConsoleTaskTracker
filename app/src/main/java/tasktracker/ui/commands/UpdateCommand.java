package tasktracker.ui.commands;

import tasktracker.model.Status;
import tasktracker.model.Task;
import tasktracker.service.TaskService;
import tasktracker.ui.ArgumentParser;
import java.util.Map;
import java.util.UUID;

public class UpdateCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        if (args.length < 2) {
            System.out.println("  Error: Task ID required.");
            System.out.println("Usage: " + getUsage());
            return;
        }
        
        try {
            UUID id = UUID.fromString(args[1]);
            
            Map<String, String> parsedArgs = ArgumentParser.parseArguments(args);
            String statusStr = ArgumentParser.getValue(parsedArgs, "status");
            
            if (statusStr == null || statusStr.isEmpty()) {
                System.out.println("  Error: Status required. Use --status NEW|IN_PROGRESS|DONE");
                return;
            }
            
            Status newStatus;
            try {
                newStatus = Status.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("  Error: Invalid status. Use: NEW, IN_PROGRESS, or DONE");
                return;
            }
            
            Task task = service.updateTaskStatus(id, newStatus);
            
            System.out.println("  Task status updated successfully!");
            System.out.println("   ID: " + task.getId());
            System.out.println("   Title: " + task.getTitle());
            System.out.println("   New Status: " + task.getStatus());
            
        } catch (IllegalArgumentException e) {
            System.out.println("  Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Error: Invalid ID format.");
        }
    }
    
    @Override
    public String getDescription() {
        return "Update task status";
    }
    
    @Override
    public String getUsage() {
        return "update <id> --status NEW|IN_PROGRESS|DONE";
    }
}