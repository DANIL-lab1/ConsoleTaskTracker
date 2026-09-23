package tasktracker.ui.commands;

import tasktracker.service.TaskService;
import tasktracker.util.ColorUtils;

public class ClearCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        service.deleteAllTasks();
        ColorUtils.printSuccess("SUCCESS: All tasks cleared!");
    }
    
    @Override
    public String getDescription() {
        return "Clear all tasks";
    }
    
    @Override
    public String getUsage() {
        return "clear";
    }
    
    @Override
    public String getCategory() {
        return "UTILITY";
    }
}