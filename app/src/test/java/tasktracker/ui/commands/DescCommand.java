package tasktracker.ui.commands;

import tasktracker.model.Task;
import tasktracker.service.TaskService;
import tasktracker.util.ColorUtils;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class DescCommand implements ICommand{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    @Override
    public void execute(String[] args, TaskService service) {
        if (args.length < 2) {
            ColorUtils.printError("Task ID required.");
            System.out.println("Usage: " + getUsage());
            return;
        }
        
        try {
            UUID id = UUID.fromString(args[1]);

            Task task = service.findTaskById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + args[1]));

            printTaskDetails(task);
        } catch (IllegalArgumentException e) {
            ColorUtils.printError(e.getMessage());
        } catch (Exception e) {
            ColorUtils.printError("Invalid ID format.");
        }
    }
    
    private void printTaskDetails(Task task) {
        System.out.println();
        System.out.println(ColorUtils.cyan("Task Details:"));
        System.out.println(ColorUtils.gray("=".repeat(50)));

        System.out.println("ID:          " + ColorUtils.gray(task.getId().toString()));
        System.out.println("Title:       " + ColorUtils.bold(task.getTitle()));
        System.out.println("Status:      " + ColorUtils.colorStatus(task.getStatus().toString()));
        System.out.println("Priority:    " + task.getPriority());
        System.out.println("Deadline:    " + (task.getDeadline() != null 
            ? task.getDeadline().toLocalDate() 
            : "-"));
        System.out.println("Assignee:    " + (task.getAssignee() != null 
            ? task.getAssignee().getName() 
            : ColorUtils.dim("Unassigned")));
        System.out.println("Created:     " + task.getCreatedAt().format(FORMATTER));
        System.out.println("Updated:     " + task.getUpdatedAt().format(FORMATTER));

        System.out.println(ColorUtils.gray("-".repeat(50)));
        System.out.println("Description:");

        String desc = task.getDescription();
        if (desc == null || desc.trim().isEmpty()) {
            System.out.println(ColorUtils.dim("(no description)"));
        } else {
            System.out.println(desc);
        }

        System.out.println(ColorUtils.gray("=".repeat(50)));
        System.out.println();
    }
    
    @Override
    public String getDescription() {
        return "Show full task details";
    }
    
    @Override
    public String getUsage() {
        return "desc <id>";
    }
    
    @Override
    public String getCategory() {
        return "TASK MANAGEMENT";
    }
}
