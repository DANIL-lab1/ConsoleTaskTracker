package tasktracker.ui.commands;

import tasktracker.model.Status;
import tasktracker.model.Task;
import tasktracker.util.ColorUtils;
import tasktracker.service.TaskService;
import tasktracker.ui.ArgumentParser;
import java.util.Map;
import java.util.UUID;

public class UpdateCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        if (args.length < 2) {
            ColorUtils.printError("ERROR: Task ID required.");
            System.out.println("Usage: " + getUsage());
            return;
        }
        
        try {
            UUID id = UUID.fromString(args[1]);
            
            Map<String, String> parsedArgs = ArgumentParser.parseArguments(args);
            String statusStr = ArgumentParser.getValue(parsedArgs, "status");
            
            if (statusStr == null || statusStr.isEmpty()) {
                ColorUtils.printError("ERROR: Status required. Use --status NEW|IN_PROGRESS|DONE");
                return;
            }
            
            Status newStatus;
            try {
                newStatus = Status.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                ColorUtils.printError("ERROR: Invalid status. Use: NEW, IN_PROGRESS, or DONE");
                return;
            }
            
            Task task = service.updateTaskStatus(id, newStatus);
            
            ColorUtils.printSuccess("Task status updated!");
            System.out.println("   ID: " + ColorUtils.gray(task.getId().toString()));
            System.out.println("   Title: " + ColorUtils.bold(task.getTitle()));
            System.out.println("   New Status: " + ColorUtils.colorStatus(task.getStatus().toString()));
            
        } catch (IllegalArgumentException e) {
            ColorUtils.printError("ERROR: " + e.getMessage());
        } catch (Exception e) {
            ColorUtils.printError("ERROR: Invalid ID format.");
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
    
    @Override
    public String getCategory() {
        return "TASK MANAGEMENT";
    }
}