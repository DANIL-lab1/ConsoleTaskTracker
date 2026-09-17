package tasktracker.ui.commands;

import tasktracker.model.Task;
import tasktracker.service.TaskService;
import tasktracker.util.ColorUtils;

public class UndoCommand implements ICommand{
    
    @Override
    public void execute(String[] args, TaskService service) {
        if (!service.hasRecentlyDeleted()) {
            ColorUtils.printWarning("Nothing to undo.");
            return;
        }
        
        try {
            Task task = service.restoreLastDeleted();
            
            ColorUtils.printSuccess("Task restored!");
            System.out.println("   ID: " + ColorUtils.gray(task.getId().toString()));
            System.out.println("   Title: " + ColorUtils.bold(task.getTitle()));
            System.out.println("   Status: " + ColorUtils.colorStatus(task.getStatus().toString()));
            System.out.println("   Priority: " + task.getPriority());
            System.out.println("   Assignee: " + (task.getAssignee() != null 
                ? task.getAssignee().getName() 
                : ColorUtils.dim("Unassigned")));
            
        } catch (IllegalStateException e) {
            ColorUtils.printError(e.getMessage());
        }
    }
    
    @Override
    public String getDescription() {
        return "Restore last deleted task";
    }
    
    @Override
    public String getUsage() {
        return "undo";
    }
    
    @Override
    public String getCategory() {
        return "TASK MANAGEMENT";
    }
}
