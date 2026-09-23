package tasktracker.ui.commands;

import tasktracker.service.TaskService;
import java.util.*;
import tasktracker.util.ColorUtils;
import tasktracker.model.Task;

public class DeleteCommand implements ICommand {
    
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
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
            
            System.out.print("Are you sure you want to delete '" + task.getTitle() + "'? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine().trim().toLowerCase();
            
            if (!answer.equals("y") && !answer.equals("yes")) {
                ColorUtils.printInfo("Deletion cancelled.");
                return;
            }
            
            service.deleteTask(id);
            
            ColorUtils.printSuccess("Task deleted!");
            System.out.println(ColorUtils.dim("TIP: Use 'undo' to restore it."));
            
        } catch (IllegalArgumentException e) {
            ColorUtils.printError(e.getMessage());
        } catch (Exception e) {
            ColorUtils.printError("Invalid ID format.");
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