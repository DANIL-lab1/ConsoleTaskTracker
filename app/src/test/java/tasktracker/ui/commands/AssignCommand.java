package tasktracker.ui.commands;

import tasktracker.model.Person;
import tasktracker.model.Task;
import tasktracker.service.TaskService;
import tasktracker.ui.ArgumentParser;
import java.util.Map;
import java.util.UUID;
import tasktracker.util.ColorUtils;

public class AssignCommand implements ICommand {
    
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
            String assigneeName = ArgumentParser.getValue(parsedArgs, "to");
            
            if (assigneeName == null || assigneeName.isEmpty()) {
                ColorUtils.printError("ERROR: Assignee name required. Use --to \"Name\"");
                return;
            }
            
            Person assignee = service.findOrCreatePerson(assigneeName);
            Task task = service.assignTask(id, assignee);
            
            System.out.println("SUCCESS: Task assigned!");
            System.out.println("   ID: " + task.getId());
            System.out.println("   Title: " + task.getTitle());
            System.out.println("   Assignee: " + assignee.getName());
            
        } catch (IllegalArgumentException e) {
            ColorUtils.printError("ERROR: " + e.getMessage());
        } catch (Exception e) {
            ColorUtils.printError("ERROR: Invalid ID format.");
        }
    }
    
    @Override
    public String getDescription() {
        return "Assign task to a person";
    }
    
    @Override
    public String getUsage() {
        return "assign <id> --to \"Person name\"";
    }
    
    @Override
    public String getCategory() {
        return "TASK MANAGEMENT";
    }
}