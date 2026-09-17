package tasktracker.ui.commands;

import tasktracker.model.Person;
import tasktracker.model.Status;
import tasktracker.model.Task;
import tasktracker.util.ColorUtils;
import tasktracker.service.TaskService;
import tasktracker.ui.ArgumentParser;
import java.util.Map;

import tasktracker.model.Priority;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class CreateCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        Map<String, String> parsedArgs = ArgumentParser.parseArguments(args);
        
        String title = ArgumentParser.getValue(parsedArgs, "title");
        String description = ArgumentParser.getValue(parsedArgs, "desc", "");
        String assigneeName = ArgumentParser.getValue(parsedArgs, "assignee");
        String statusStr = ArgumentParser.getValue(parsedArgs, "status");
        String priorityStr = ArgumentParser.getValue(parsedArgs, "priority");
        String deadlineStr = ArgumentParser.getValue(parsedArgs, "deadline");
        
        if (title == null || title.isEmpty()) {
            System.out.println("ERROR: Title is required. Use --title \"Task name\"");
            System.out.println("Usage: " + getUsage());
            return;
        }
        
        try {
            // ===== Status =====
            Status status = Status.NEW;
            if (statusStr != null && !statusStr.isEmpty()) {
                try {
                    status = Status.valueOf(statusStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    ColorUtils.printWarning("Invalid status. Using NEW.");
                }
            }
            
            // ===== Priority =====
            Priority priority = Priority.MEDIUM;
            if (priorityStr != null && !priorityStr.isEmpty()) {
                try {
                    priority = Priority.valueOf(priorityStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    ColorUtils.printWarning("Invalid priority. Using MEDIUM.");
                }
            }
            
            // ===== Deadline =====
            LocalDateTime deadline = null;
            if (deadlineStr != null && !deadlineStr.isEmpty()) {
                try {
                    deadline = LocalDate.parse(deadlineStr).atStartOfDay();
                } catch (DateTimeParseException e) {
                    ColorUtils.printWarning("Invalid deadline format. Use YYYY-MM-DD.");
                }
            }
            
            // ===== Assignee =====
            Person assignee = null;
            if (assigneeName != null && !assigneeName.isEmpty()) {
                assignee = service.findOrCreatePerson(assigneeName);
            }
            
            // ===== Create task =====
            Task task = service.createTask(title, description, assignee);
            
            // Apply status, priority, deadline
            if (status != Status.NEW) {
                task.setStatus(status);
            }
            task.setPriority(priority);
            if (deadline != null) {
                task.setDeadline(deadline);
            }
            service.updateTask(task);
            
            // ===== Output =====
            ColorUtils.printSuccess("Task created!");
            System.out.println("   ID: " + ColorUtils.gray(task.getId().toString()));
            System.out.println("   Title: " + ColorUtils.bold(task.getTitle()));
            System.out.println("   Status: " + ColorUtils.colorStatus(task.getStatus().toString()));
            System.out.println("   Priority: " + task.getPriority());
            System.out.println("   Deadline: " + (task.getDeadline() != null ? task.getDeadline().toLocalDate() : "none"));
            System.out.println("   Assignee: " + (task.getAssignee() != null ? task.getAssignee().getName() : ColorUtils.dim("Unassigned")));
            
        } catch (Exception e) {
            System.out.println("ERROR creating task: " + e.getMessage());
        }
    }
    
    @Override
    public String getDescription() {
        return "Create a new task";
    }
    
    @Override
    public String getUsage() {
        return "create --title \"Task name\" --desc \"Description\" --assignee \"Name\" --status NEW|IN_PROGRESS|DONE --priority HIGH|MEDIUM|LOW --deadline YYYY-MM-DD";
    }
    
    @Override
    public String getCategory() {
        return "TASK MANAGEMENT";
    }
}