package tasktracker.ui.commands;

import tasktracker.service.TaskService;
import tasktracker.util.ColorUtils;

public class ExitCommand implements ICommand {
    
    @Override
    public void execute(String[] args, TaskService service) {
        ColorUtils.cyan("Goodbye!");
        System.exit(0);
    }
    
    @Override
    public String getDescription() {
        return "Exit the application";
    }
    
    @Override
    public String getUsage() {
        return "exit";
    }
    
    @Override
    public String getCategory() {
        return "UTILITY";
    }
}