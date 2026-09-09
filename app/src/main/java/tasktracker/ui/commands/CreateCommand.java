package tasktracker.ui.commands;

import tasktracker.model.Person;
import tasktracker.model.Status;
import tasktracker.model.Task;
import tasktracker.service.TaskService;
import tasktracker.ui.ArgumentParser;
import java.util.Map;

public class CreateCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        Map<String, String> parsedArgs = ArgumentParser.parseArguments(args);
        
        String title = ArgumentParser.getValue(parsedArgs, "title");
        String description = ArgumentParser.getValue(parsedArgs, "desc", "");
        String assigneeName = ArgumentParser.getValue(parsedArgs, "assignee");
        String statusStr = ArgumentParser.getValue(parsedArgs, "status");
        
        if (title == null || title.isEmpty()) {
            System.out.println("  Error: Title is required. Use --title \"Task name\"");
            System.out.println("Usage: " + getUsage());
            return;
        }
        
        try {
            Status status = Status.NEW;
            if (statusStr != null && !statusStr.isEmpty()) {
                try {
                    status = Status.valueOf(statusStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("  Warning: Invalid status. Using NEW.");
                }
            }
            
            Person assignee = null;
            if (assigneeName != null && !assigneeName.isEmpty()) {
                assignee = service.findOrCreatePerson(assigneeName);
            }
            
            Task task = service.createTask(title, description, assignee);
            
            if (status != Status.NEW) {
                service.updateTaskStatus(task.getId(), status);
            }
            
            System.out.println("  Task created successfully!");
            System.out.println("   ID: " + task.getId());
            System.out.println("   Title: " + task.getTitle());
            System.out.println("   Status: " + task.getStatus());
            System.out.println("   Assignee: " + (assignee != null ? assignee.getName() : "Unassigned"));
            
        } catch (Exception e) {
            System.out.println("  Error creating task: " + e.getMessage());
        }
    }
    
    @Override
    public String getDescription() {
        return "Create a new task";
    }
    
    @Override
    public String getUsage() {
        return "create --title \"Task name\" --desc \"Description\" --assignee \"Name\" --status NEW|IN_PROGRESS|DONE";
    }
}